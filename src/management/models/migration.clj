(ns management.models.migration
  (:require [clojure.java.jdbc :as sql])
  (:use     [management.config]))

(defn create-user-table [db-conn]
  (sql/with-connection db-conn
    (try
      (sql/create-table
       :user
       [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"]
       [:fname "varchar(25)"]
       [:lname "varchar(25)"])
      (catch Exception _))))

(defn -main []
  (print "Creating database structure...") (flush)
  (let [root-dir "/Users/matthewburns/github/florish-online"
        cfg (new-config {:cfg-file-path (str root-dir "/conf/system_config.json")})
        {:keys [host port db-user db-password db-name]} (-> (read-config cfg) :management-gui :account-database)
        db-conn {:classname "com.mysql.jdbc.Driver"
                     :subprotocol "mysql"
                     :subname (str "//" host ":" port "/" db-name)
                     :user db-user
                     :password db-password}]
    (create-user-table db-conn)))

