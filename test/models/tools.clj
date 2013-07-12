(ns models.tools
  (:require [clojure.java.jdbc :only[with-server with-connection with-open]:as sql]
             [expectations])
  (:use [clojure.core :exclude[values]]
        ;;        [management.models.tools :only(dashboard-config)]
        [management.models.tools]        
        [management.database :only[drop-database create-database create-management-database
                                   table-builder]]
        [management.models.orm-spec  :only [user profile email grouping] :as orm]
        [korma.core :only [defentity database insert values select where with fields join select* sql-only]]        
        [korma.db :only [defdb mysql]]
        [clojurewerkz.scrypt.core :as crypto]))

(def db-name "test_mgmt")
(def db-user "root")
(def db-host  "127.0.0.1")
(def admin-db "test")
(def db-name "mgmt")
(def db-port 3306)
(def db-user  "root")
(def db-password "")

(defdb mysql-db (mysql {:db db-name :user db-user :password db-password :host db-host}))

(def test-db-dsn {:classname "com.mysql.jdbc.Driver"
                  :subprotocol "mysql"
                  :subname (str "//" db-host ":" db-port "/" admin-db)
                  :user "root"
                  :password ""})


(def mgmt-db-dsn {:classname "com.mysql.jdbc.Driver"
                  :subprotocol "mysql"
                  :subname (str "//" db-host ":" db-port "/" db-name)
                  :user "root"
                  :password ""})

(drop-database test-db-dsn db-name)
(create-database test-db-dsn db-name)
(create-management-database mgmt-db-dsn)

(def test-user {
                :username "test1"
                :password (crypto/encrypt "test123" 16384 8 1)
                :status "active"
                :title "tester"
                :signed_up_on "2013052412000000"
                :last_sign_in "2013052412000000"})

(def complete-test-profile {:id 1
                            :status "active"
                            :user_id 1
                            :tag_name "test profile 1"
                            :query_uri "user/profiles/1"})

(def test-profile (dissoc complete-test-profile :tools :id :status))
(dashboard-config {:user-id 1})
(insert user (values test-user))
(crypto/verify "est123" (:password (first (select orm/user (where {:id 1})))))
(insert profile (values test-profile))

(select profile)
(def user-data (first (select user (with profile) (where {:id 1}))))
(def active-profile (get-profile-settings user-data))
(def snippet-functions (map :display-with (-> active-profile :tools :dashboard :display-tuple)))
(def gather-functions (map :gather-with (-> active-profile :tools :dashboard :display-tuple)))
(def display-order (map :order (-> active-profile :tools :dashboard :display-tuple)))
(def model-view-tuple (apply map (fn [x y z] {:view   '(x) :model ((eval y)) :order z}) [snippet-functions gather-functions display-order]))
(default-stats)
(default-stats-summary)
(current-oppurtunities)

(def labels (sort-by :order model-view-tuple))
(map (fn [x] {:view ((eval (:view x)))}) labels)
(map (fn [x] {:model ((eval (:model x)))}) labels)

(map (fn [x] {:model (resolve (symbol (:model x)))}) labels)
((resolve (last snippet-functions)))
(def args )
(def foo (last gather-functions))
((resolve (symbol foo)))
((resolve (first gather-functions)))


(when-let [fun (ns-resolve *ns* (symbol foo))]
          (apply fun args))

       

(~foo)
(eval foo)
(apply foo)
(default-stats)
  
(select user (with email))
(fields :username :email.address)
        (join email (= :email.id_user :id))        

(insert email (values [{:user_id 1 :address "mburns@gmail.com"}
                       {:user_id 1 :address "test-sales@marketingwithgusto.com"}
                       {:user_id 1 :address "test-systems@marketingwithgusto.com"}
                       {:user_id 1 :address "test-operations@marketingwithgusto.com"}]))                       

(insert grouping (values [{:id 1 :type "user" :group-name "ham users"}
                       {:id 2 :type "account" :group-name "ham account"}
                       {:id 3 :type "admin" :group-name "ham administrators"}]))




(def entry (log-lead good-test-lead))
(def retreived-record (first (kdb/select lead_log
                                         (kdb/fields :full_name :email :phone)
                                         (kdb/where {:id  (:id entry)}))))

(expect true (= (:phone-number good-test-lead) (:phone retreived-record)))

(def support-entry (log-support-lead support-group-test-lead))
(def retreived-record (first (kdb/select lead_log
                                         (kdb/fields :full_name :email :phone :postal_code)
                                         (kdb/where {:id (:id support-entry)}))))

(expect true (= (:postal-code support-group-test-lead) (str (:postal_code retreived-record))))

