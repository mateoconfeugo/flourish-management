(ns management.controllers.user
  (:use [compojure.core :only (defroutes GET POST)]
        [management.config :only(new-config read-config)])
  (:require [cemerick.friend :as friend]
            (cemerick.friend [workflows :as workflows]
                             [credentials :as creds]))
  (:require [clojure.string :as str]
            [ring.util.response :as ring]
            [management.views.users :as view]
            [management.models.user :as model]))

(defn get-db-conn []
  "TODO: Replace this with a more generic function"
  (let [root-dir (str "/Users/matthewburns/github/florish-online")
        cfg (new-config {:cfg-file-path (str root-dir "/conf/system_config.json")})
        {:keys [host port db-user db-password db-name]} (-> (read-config cfg) :management-gui :account-database)]
    {:classname "com.mysql.jdbc.Driver"
     :subprotocol "mysql"
     :subname (str "//" host ":" port "/" db-name)
     :user db-user
     :password db-password}))

(defn index []
  (view/index (model/all (get-db-conn))))

(defn all-users []
  (view/users-list (model/all (get-db-conn))))

(derive ::admin ::user)


(defn create [user]
  (when-not (str/blank? (:first-name user))
    (model/create user (get-db-conn)))
  (ring/redirect "/all"))

(defroutes user-routes
  (GET  "/user/:id" [id]  (all-users))
  (POST "/user" [fname lname] (create {:first-name fname :last-name lname})))