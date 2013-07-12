(ns management.database
  (:require [clojure.java.jdbc :only[with-connection create-table
                                     with-server with-connection with-open]:as sql])
  (:use [korma.core :only [defentity database insert values select where]]        
        [korma.db :only [defdb mysql]]))


(defmacro table-builder
  [dsn table-name & statements]
  `(sql/with-connection ~dsn
     (sql/create-table ~table-name ~@statements)))

(def tables [:feed_partner :version :release :supported_release :user :population :community :enterprise
             :company :organization :group :clique :business_entity :email :address :state :business_account
             :landing_site :account :partner :feed :feed_revenue_share :feed_distribution  :feed_partner_lock_data
             :user_to_disable :owner :address :contact :account_standing :account_user :credit_card
             :advertiser_payment :spend_limit :keyword :keyword_bucket :destination :ad_group :campaign :targeting_method
             :dimension :colum_order :column_group :report :report_column :account :admin-account])

(defn drop-database
  [dsn name]
  (sql/with-connection dsn
    (with-open [s (.createStatement (sql/connection))]
      (.addBatch s (str "DROP DATABASE IF EXISTS " name))
      (seq (.executeBatch s)))))

(defn create-database
  [dsn name]
  (sql/with-connection dsn
    (with-open [s (.createStatement (sql/connection))]
      (.addBatch s (str "DROP DATABASE IF EXISTS " name))
      (.addBatch s (str "create database " name))
      (seq (.executeBatch s)))))

(defn create-management-database
  [dsn]
  "Build schema for the management database"
  (table-builder dsn  :profile
                 [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"]
                 [:user_id "INTEGER(11) NOT NULL"]
                 [:tag_name "VARCHAR(32) NOT NULL"]
                 [:query_uri "VARCHAR(255) NOT NULL"])
    
    (table-builder dsn :user
                  [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"]
                  [:username "VARCHAR(50) NOT NULL"]
                  [:password "VARCHAR(225) NOT NULL"]
;;                  [:primary_email "VARCHAR(150) NOT NULL"]
;;                  [:activation_token "VARCHAR(225) NOT NULL"]
;;                  [:last_activation_request  "INT(11) NOT NULL"]
;;                  [:lost_password_request "TINYINT(1) NOT NULL"]
                  [:status "ENUM('active', 'inactive', 'suspended', 'expired', 'banded', 'banished') NOT NULL"]
                  [:title  "VARCHAR(150) "]
                  [:signed_up_on  "DATETIME NOT NULL"]
                  [:last_sign_in "DATETIME NOT NULL"])

    (table-builder dsn :feed_partner
                  [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"]
                   [:tag_name "VARCHAR(25)"])

    (table-builder dsn :feed
                  [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"]
                  [:id_partner "varchar(25)"]
                  [:tag_name "varchar(32)"]       
                  [:query_uri "varchar(255)"])
    (table-builder dsn :population
                   [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"]
                   [:admin_account_id :integer "NOT NULL"]
                   [:name "VARCHAR(32)"]
                   [:description "VARCHAR(255)"])                   

    (table-builder dsn :version
                   [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"])
    
    (table-builder dsn :app_release
                   [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"])
    (table-builder dsn :supported_release
                  [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"])
    (table-builder dsn :community
                  [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"])
    (table-builder dsn :enterprise
                  [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"])
    (table-builder dsn :company
                  [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"])
    (table-builder dsn :organization
                  [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"])
    (table-builder dsn :grouping
                   [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"]
                   [:name "VARCHAR(25)"])
    (table-builder dsn :clique
                  [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"])
    (table-builder dsn :business_entity
                  [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"])
    (table-builder dsn :email
                   [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"]
                   [:user_id :integer "NOT NULL"]
                   [:address "VARCHAR(300)"])
    (table-builder dsn :address
                  [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"])
    (table-builder dsn :state
                  [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"])
    (table-builder dsn :business_account
                  [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"])
    (table-builder dsn :landing_site
                  [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"])
    (table-builder dsn :account
                   [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"])
    (table-builder dsn :admin_account
                  [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"])
    (table-builder dsn :partner
                  [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"])
    (table-builder dsn :feed_revenue_share
                  [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"])
    (table-builder dsn :feed_distribution_share
                  [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"])
    (table-builder dsn :feed_partner_lock_data
                  [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"])
    (table-builder dsn :user_to_disable
                  [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"])
    (table-builder dsn :owner
                  [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"])
    (table-builder dsn :contact
                  [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"])
    (table-builder dsn :account_standing
                  [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"])
    (table-builder dsn :account_user
                  [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"])
    (table-builder dsn :credit_card
                  [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"])
    (table-builder dsn :advertiser_payment
                  [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"])
    (table-builder dsn :spend_limit
                  [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"])
    (table-builder dsn :keyword
                  [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"])
    (table-builder dsn :keyword_bucket
                  [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"])
    (table-builder dsn :destination
                  [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"])
    (table-builder dsn :ad_group
                  [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"])
    (table-builder dsn :campaign
                  [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"])
    (table-builder dsn :targeting_method
                  [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"])
    (table-builder dsn :dimension
                  [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"])
    (table-builder dsn :colum_order
                  [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"])
    (table-builder dsn :column_group
                  [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"])
    (table-builder dsn :report
                  [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"])
    (table-builder dsn :report_column
                  [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"])
    )



(defn drop-table [table dsn]
  (sql/with-connection dsn  
    (try
      (sql/drop-table (keyword table))
      (catch Exception _))))

(defn create-feed-partner-table [dsn]
  (sql/with-connection dsn
    (try
      (sql/create-table
       :feed_partner
       [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"]
       [:tag_name "varchar(25)"])
      (catch Exception _))))

(defn create-feed-table [dsn]
  (sql/with-connection dsn
    (try
      (sql/create-table
       :feed
       [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"]
       [:partner_id "varchar(25)"]
       [:tag_name "varchar(32)"]       
       [:query_uri "varchar(255)"])
      (catch Exception _))))

(defn drop_management_database
  "remove the database the has all the feed partner data"
  [dsn]
  (let [tables [""]]
    (doseq [t tables]
      (drop-table t dsn))))

(comment
(defn populate-mgmt-db []
  "adds test data to the db"
  (select  feed_partner
           (fields "tag_name")
           (where (or {:id 1}
                      {:tag_name "google"})))))
