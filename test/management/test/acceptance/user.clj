(ns system-level
  (:use [pallet.action :only[with-action-options]]
        [pallet.configure :only[compute-service]]
        [pallet.api :only[converge]]
        [landing-site.handler]
        [landing-site.config :only [db-settings delivery-settings]]
        [landing-site.groups.database :only[database-group]]
        [landing-site.groups.delivery :only[delivery-group]]        
        [expectations]
        [clj-webdriver.taxi :only [set-driver! to click exists? input-text submit quit] :as scraper]
        [clojure.core]))

;; Spin up a system of virtual machines to test with
(def service (compute-service (:provider db-settings)))
(def management-nodes (converge {database-group 1} :compute service))
(def management-node  ((first (@management-nodes :targets)) :node))
(def management-ip (.primary-ip db-node))
(def monitoring-nodes (converge {monitoring-group 1} :compute service))
(def monitoring-node  ((first (@db-nodes :targets)) :node))
(def monitoring-ip (.primary-ip db-node))
(def delivery-nodes (converge {delivery-group 1} :compute service))
(def delivery-node  ((first (@db-nodes :targets)) :node))
(def delivery-ip (.primary-ip db-node))

(defn wait-sec [n] (Thread/sleep (* 1000 n)))
(def test-port 8087)
(def signup-uri "/")
;;(def app-server (landing-site.handler/start test-port))
(def test-user {:email "matthewburns@gmail.com" :full_name "bob smit" :phone "8182546028"})

;; ACCEPTANCE TEST: User Signup
;; Register user save in database navigate to wizard page
;; Start up a browser and go to user sign-up
(def app-uri (str "http://" management-ip ":" test-port signup-uri))
(scraper/set-driver! {:browser :firefox} app-uri)
;; input lead info
(scraper/input-text "#username" (:username test-user) )
(scraper/input-text "#password" (:password test-user))
(scraper/input-text "#verify_password" (:password test-user))
(scraper/input-text "#email" (:email test-user))
(scraper/click "#signup-form-submit")
;; (.stop app-server)

(def user-record (first (kdb/select user
                                  (kdb/fields :id :username :email)
                                  (kdb/where {:username (:username test-user)}))))

(def expected-url "/user/welcome.html")
(expect true (= (:username test-user) (:username user-record)))
(expect true (= (current-url) expected-url))

(kdb/delete user
            (kdb/where {:username (:username test-user)}))
(kdb/delete lead_log
            (kdb/where {:id (:id db-record)}))

(power-down lsbs-provider-host)
(destroy lsbs-provider-host)

