(ns management.models.orm-spec
  "storing and manipulating leads"
;;  (:import [com.google.i18n.phonenumbers PhoneNumberUtil NumberParseException]
  ;;           [java.util.zip  DataFormatException])
  (:use [metis.core]
        [korma.core :only [defentity database insert values has-many many-to-many
                           transform belongs-to has-one fields table prepare pk
                           subselect where belongs-to limit aggregate order]]
        [riemann.client :only [send-event tcp-client]]        
        [korma.db :only [defdb mysql]])
  (:require [clojure.core]))

;; TODO: replace this with config
;;(def db-name "test_mgmt")
(def db-user "root")
(def db-host  "127.0.0.1")
(def admin-db "test")
(def db-name "mgmt")
(def db-port 3306)
(def db-user  "root")
(def db-password "test123")

(defdb mgmt-db (mysql {:db db-name :user db-user :password db-password :host db-host}))


(declare  profile feed-partner version release supported-release user population community enterprise
          company organization grouping clique business-entity email address state business-account
          landing-site account partner feed feed-revenue-share feed-distribution  feed-partner-lock-data
          user-to-disable owner address contact account-standing user-account credit-card
          advertiser-payment spend-limit keyword-phrase keyword-bucket destination ad-group campaign targeting-method
          dimension colum-order column-group report report-column admin-account authorization-role business-role)

;; belongs to
(declare supported-app-release user-admin-account)
(declare user-business-role user-business-account user-landing-site grouping-population grouping-community user-clique)

(defentity profile
  (pk :id)
  (table :profile)
  (database mgmt-db)
  (fields :id :id_user :tag_name :query_uri)
  (belongs-to user))  

(defentity admin-account
  (pk :id)
  (table :admin_account)  
  (database mgmt-db))

(defentity email
  (pk :id)
  (table :email)
  (database mgmt-db)     
  (fields :id :id_user :address)
  (belongs-to user {:fk :id_user}))

(defentity grouping
  (pk :id)
  (table :group)
  (database mgmt-db)
  (fields {:id :name})
  (many-to-many user :user_grouping)  
  (many-to-many community :community_grouping)
  (many-to-many population :population_grouping)  
  (belongs-to admin-account)
  (has-many clique)
  (has-many business-entity))

(defentity supported-app-release
  (table :supported_release)
  (database mgmt-db)
  (fields :version-id  :deployed-on :released-on))

(defentity version
  (table :version)
  (database mgmt-db)
  (fields :db-version  :deployed-on)
  (has-one release)
  (has-many supported-app-release))

(defentity release
  (table :releases)
  (fields :released-on :tag))


(defentity user
  (pk :id)
  (table :user)
  (database mgmt-db) 
  (fields  :signed_up_on :status :username :last_sign_in :password :username :id :title)
  (prepare (fn [{last :last :as v}]
             (if last
               (assoc v :last (clojure.string/upper-case last)) v)))

  (transform (fn [{first :first :as v}]
               (if first
                 (assoc v :first (clojure.string/capitalize first)) v)))
  (has-one profile)  
  (has-one address)
  (has-one user-account)  
  (has-many email)
  (belongs-to admin-account)
  (has-many grouping)
  (has-many authorization-role)
  (many-to-many business-role :user_business_role)    
  (many-to-many business-account :user_business_account)    
  (many-to-many landing-site :user_landing-site))

;;(defentity active-users (table (subselect user (where {:active true})) :activeUsers))
;;(defentity active-landing-sites (table (subselect landing-site (where {:status :active})) :activeLandingSites))

(defentity population
  (pk :id)
  (table :population)
  (database mgmt-db) 
  (fields :name :description)
  (belongs-to admin-account)  
  (has-many business-entity)  
  (has-many grouping)
  (has-many community))

(defentity community
  (pk :id)
  (table :community)
  (database mgmt-db) 
  (fields :name :description)
  (belongs-to admin-account)
  (has-many business-entity)  
  (has-many grouping))

(defentity enterprise
  (pk :id)
  (table :enterprise)
  (database mgmt-db) 
  (fields :name :description)
  (belongs-to company)
  (has-many organization)
  (has-many business-entity))

(defentity company
   (pk :id)
   (table :company)
   (database mgmt-db)
   (has-many address)
   (fields :name :description)
   (has-many business-entity)
   (has-many grouping))

(defentity organization
  (pk :id)
  (table :organization)
  (database mgmt-db)
  (has-many address)
  (fields :name :description)
  (belongs-to admin-account)
  (has-many grouping))  

(defentity clique
  (pk :id)
  (table :clique)
  (database mgmt-db)
  (fields :name :description)  
  (belongs-to admin-account)
  (has-many user-account)  
  (has-many business-role)    
  (has-many authorization-role)
  (has-many business-account)
  (has-many business-entity)
  (many-to-many grouping :clique_grouping)
  (many-to-many user :user_grouping))


(defentity business-entity
  (pk :id)
  (table :business_entity)
  (database mgmt-db) 
  (fields :group-id :uri :type :name))

(defentity address
  (pk :id)
  (table :address)
  (database mgmt-db)     
  (belongs-to user)
  (belongs-to state {:fk :id_state}))

(defentity state
  (pk :id)
  (table :state_st) ;; sets the table to "state_st"  
  (database mgmt-db)     
  (has-many address))

(defentity business-account
  (pk :id)
  (table :business_account) ;; sets the table to "state_st"  
  (database mgmt-db)     
  (has-many user))

(defentity user-account
  (pk :id)
  (table :business_account) ;; sets the table to "state_st"      
  (has-one user))

(defentity landing-site
  (pk :id)
  (table :landing_site)  
  (many-to-many user :users_landing_site))


(defentity partner
  (pk :id)
  (table :partner)  
  (database mgmt-db)
  (has-one account))

(defentity feed
  (pk :id)
  (table :feed)  
  (database mgmt-db))

(defentity feed-revenue-share
  (pk :id)
  (table :feed_revenue_share)  
  (database mgmt-db)
  (belongs-to feed))

(defentity feed-distribution
  (pk :id)
  (table :feed_distribution)
  (database mgmt-db)
  (belongs-to feed)
  (belongs-to account))

(defentity feed-partner-lock-data
  (pk :id)
  (table :feed_partner_lock_data)
  (database mgmt-db))

(defentity users-to-disable
  (pk :id)
  (table :users_to_disable)
  (database mgmt-db)
  (belongs-to user))

(defentity owner
  (pk :id)
  (table :owner)
  (database mgmt-db)
  (has-one user))

(defentity address
  (pk :id)
  (table :address)
  (database mgmt-db))

(defentity contact
  (pk :id)
  (table :contact)  
  (database mgmt-db)
  (has-one address))

(defentity account-user
  (pk :id)
  (table :account_user)  
  (database mgmt-db)
  (has-one user)
  (has-one account))

(defentity account-standing
  (pk :id)
  (table :account_standing)  
  (database mgmt-db)  
  (has-one account))

(defentity credit-card
  (pk :id)
  (table :credit_card)  
  (database mgmt-db))

(defentity advertiser-payment
  (pk :id)
  (table :advertiser_payment)    
  (database mgmt-db))

(defentity spend-limit
  (pk :id)
  (table :spend_limit)      
  (database mgmt-db))

(defentity keyword-phrase
  (pk :id)
  (table :keyword)
  (database mgmt-db)
  (belongs-to ad-group))

(defentity keyword-bucket
  (pk :id)
  (table :keyword_bucket)
  (database mgmt-db)
  (has-many keyword-phrase))

(defentity destination
  (pk :id)
  (table :destination)
  (database mgmt-db)
  (has-one account))

(defentity ad-group
  (pk :id)
  (table :ad_group)
  (database mgmt-db)
  (has-one targeting-method)
  (has-many keyword-phrase))

;;  (has-many ad-units)

(defentity campaign
  (pk :id)
  (table :campaign)
  (database mgmt-db)
  (has-many ad-group))

(defentity targeting-method
  (pk :id)
  (table :targeting_method)
  (database mgmt-db))

(defentity dimension
  (pk :id)
  (table :dimension)
  (database mgmt-db)
  (belongs-to report))

(defentity column-order
  (pk :id)
  (table :colum_order)
  (database mgmt-db)
  (belongs-to report))

(defentity column-group
  (pk :id)
  (table :column_group)
  (has-one report))

(defentity report
  (pk :id)
  (table :report)
  (database mgmt-db)
  (has-one owner)
  (has-one column-order)
  (has-many column-group)  
  (has-many user)
  (has-many dimension)  
  (belongs-to account)
  (has-many email))

(defentity column
  (pk :id)
  (table :column)  
  (database mgmt-db))

(defn winnowing-fn [] {})
(comment
(def default-publisher-report (-> select* publisher-report
                                  (with user)
                                  (with column-order)
                                  (with column-groups)
                                  (with dimension)
                                  (with clicks)
                                  (with impressions)
                                  (fields :id :title)
                                  (aggregate (aggregation-list-fn ))
                                  (where  (winnowing-fn)
                                          (join dimension-value (= :dimension.param_id :param.id)))
                                  (where {:posts.id [in (subselect dimension-value
                                                                   (where {:param.id :dimension.param_id}))]})
                                  (order :column-order.cardinality :ASC)
                                  (group :column-groups)
                                  (limit (limit-fn))))

(def default-advertiser-report (-> select* report
                                   (with user)
                                   (with column-order)
                                   (with column-groups)
                                   (with dimension)
                                   (with impressions)
                                   (with clicks)
                                   (fields :id :title)
                                   (aggregate (aggregation-list-fn ))
                                   (where  (winnowing-fn)                    ;; returns a map
                                           (join dimension-value (= :dimension.param_id :param.id)))
                                   (where {:posts.id [in (subselect dimension-value
                                                                    (where {:param.id :dimension.param_id}))]})
                                   (order :column-order.cardinality :ASC)
                                   (group :column-groups)
                                   (limit (limit-fn))))

(def advertiser-adgroup-default (-> default-advertiser-report
                                    (fields :status :default-max-cpc :clicks :impressions :ctr :average-cpc :cost :average-position)))
)