(ns views.tools
  (:use [management.views.tools]
        [management.views.snippets]
        [management.models.tools]
        [management.models.orm-spec :as orm :only [user profile]]        
        [expectations]
        [korma.core :only [defentity database insert values select where with] :as kdb]
        [korma.db :only [defdb mysql]]
        [flourish-common.web-page-utils :only [run-server render-to-response render-request
                                               render-snippet render-to-response render]]))

;; UNIT TESTS
(def test-user-id 1)
(def display-map (dashboard-config {:user-id test-user-id}))
(keys display-map)
(rest display-map)
(:menu-data display-map)
(keys (first  display-map))
(def user-dude (first (select user (with profile) (where {:id 1}))))
(keys (get-profile-settings user-dude))
(def test-profile (get-profile-settings user-dude))
(keys test-profile)
(def test-snip-fn (map :display-with (-> test-profile :tools :dashboard :display-tuple)))
(def test-gather-fn (map :gather-with (-> test-profile :tools :dashboard :display-tuple)))
(def test-display-name-fn (map :display-name (-> test-profile :tools :dashboard :display-tuple)))
(def test-display-order (map :order (-> test-profile :tools :dashboard :display-tuple)))         
(def test-model-view-tuple (apply map (fn [x y z w] {:view x :model ((eval y)) :order z :display-name w}) [test-snip-fn test-gather-fn test-display-order test-display-name-fn]))
(sort-by :order test-model-view-tuple)
  

(def display-object (first display-map))
(def test-snippet (:view (first display-map)))
(:order display-object)
(def builder-fn (:view display-object))
(def model (:model display-object))
(render-snippet ((eval builder-fn) model))
(render-snippet (default-stats-summary (:model (first display-map))))
(render-snippet (management.views.snippets/landing-site-stats-summary (:model (last display-map))))
(render (user-dashboard {:display-map display-map}))
((:view (first display-map)) {})
(def html-output (user-dashboard ))

;; COMPONENT TESTS
(def test-request {:request-method :get :uri "/mgnt/user" :headers {} :params {:id 1}})
(expect true (= (:status (app (request :get (:uri test-request)))) 200))

;; ACCEPTANCE TESTS
(def test-port 8087)
(def test-uri "/")
(def app-server (start 8087))
(scraper/set-driver! {:browser :firefox})


;; landing site design tool tests

;; landing site marketing tool tests

;; landing site reporting tool tests

;; landing site subscription tool tests

;; input lead info
(scraper/to (str "http://localhost:" test-port test-uri))
(scraper/input-text "#lead_full_name" "test matt")
(scraper/input-text "#lead_phone" "8182546027")
(scraper/input-text "#lead_email" "matthewburns@gmail.com")
(scraper/click "#lead-form-submit")
(scraper/quit)
(.stop app-server)
