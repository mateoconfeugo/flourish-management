(ns management.models.user
  (:require [clojure.java.jdbc :as sql]))

(defn all [db-conn]
  (sql/with-connection db-conn
    (sql/with-query-results results
      ["select * from user order by id desc"]
      (into [] results))))

(defn create [user db-conn]
  (sql/with-connection db-conn
    (let [fname (:first-name user)
          lname (:last-name user)]
      (sql/insert-values :user [:fname :lname] [fname lname]))))