(ns db-configs
  (:use [clojure.test :only [deftest is testing]]
        [expectations]
        [korma.db :only [connection-pool defdb get-connection h2
                         mysql postgres sqlite3]]))

(def db-config-with-defaults
  {:classname "org.h2.Driver"
   :subprotocol "h2"
   :subname "mem:db_connectivity_test_db"
   :user "bob"
   :password "test123"})

(def db-config-with-options-set
  {:classname "org.h2.Driver"
   :subprotocol "h2"
   :subname "mem:db_connectivity_test_db"
   :excess-timeout 99
   :idle-timeout 88
   :minimum-pool-size 5
   :maximum-pool-size 20})

(deftest connection-pooling-default-test
  (let [pool (connection-pool db-config-with-defaults)
        datasource (:datasource pool)]
    (is (= "org.h2.Driver" (.getDriverClass datasource)))
    (is (= "jdbc:h2:mem:db_connectivity_test_db" (.getJdbcUrl datasource)))
    (is (= "bob" (.getUser datasource)))
    (is (= "test123" (.getPassword datasource)))

    (is (= 1800 (.getMaxIdleTimeExcessConnections datasource)))
    (is (= 10800 (.getMaxIdleTime datasource)))
    (is (= 3 (.getMinPoolSize datasource)))
    (is (= 15 (.getMaxPoolSize datasource)))))

(deftest connection-pooling-test
  (let [pool (connection-pool db-config-with-options-set)
        datasource (:datasource pool)]
    (is (= 99 (.getMaxIdleTimeExcessConnections datasource)))
    (is (= 88 (.getMaxIdleTime datasource)))
    (is (= 5 (.getMinPoolSize datasource)))
    (is (= 20 (.getMaxPoolSize datasource)))))

(deftest spec-with-missing-keys-returns-itself
  (defdb valid {:datasource :from-app-server})
  (is (= {:datasource :from-app-server} (get-connection valid))))

(def h2-defaults {:classname "org.h2.Driver"
                  :subprotocol "h2"
                  :subname "h2.db"
                  :make-pool? true})

(def h2-options {:db "db"
                 :classname "org.h2.Driver"
                 :subprotocol "h2"
                 :subname "db"
                 :make-pool? false})

(def mysql-defaults {:classname "com.mysql.jdbc.Driver"
            :subprotocol "mysql"
            :subname "//localhost:3306/"
            :delimiters "`"
                     :make-pool? true})

(def mysql-options {:db "db"
            :port "port"
            :host "host"
            :classname "com.mysql.jdbc.Driver"
            :subprotocol "mysql"
            :subname "//host:port/db"
            :delimiters "`"
            :make-pool? false})
    
;;; DB spec creation fns
(deftest test-h2
  (testing "h2 - defaults" (is (= h2-defaults (h2 {}))))
  (testing "h2 - options selected" (is (= h2-options (h2 {:db "db" :make-pool? false})))))

(deftest test-mysql
  (testing "mysql - defaults"
    (is (= mysql-defaults (mysql {}))))
  (testing "mysql - options selected"
    (is (= mysql-options  (mysql {:host "host" :port "port" :db "db" :make-pool? false})))))

(expect true (nil? (test-mysql)))
(expect true (nil? (test-h2)))

(deftest test-postgres
  (testing "postgres - defaults"
    (is (= {:classname "org.postgresql.Driver"
            :subprotocol "postgresql"
            :subname "//localhost:5432/"
            :make-pool? true}
           (postgres {}))))
  (testing "postgres - options selected"
    (is (= {:db "db"
            :port "port"
            :host "host"
            :classname "org.postgresql.Driver"
            :subprotocol "postgresql"
            :subname "//host:port/db"
            :make-pool? false}
           (postgres {:host "host"
                      :port "port"
                      :db "db"
                      :make-pool? false})))))

(deftest test-sqlite3
  (testing "sqlite3 - defaults"
    (is (= {:classname "org.sqlite.JDBC"
            :subprotocol "sqlite"
            :subname "sqlite.db"
            :make-pool? true}
           (sqlite3 {}))))
  (testing "sqlite3 - options selected"
    (is (= {:db "db"
            :classname "org.sqlite.JDBC"
            :subprotocol "sqlite"
            :subname "db"
            :make-pool? false}
           (sqlite3 {:db "db" :make-pool? false})))))

