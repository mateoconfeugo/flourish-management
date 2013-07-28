(ns cms.resource.collections
  (:use [clojure.walk]
        [cms.resources.item]
        [cms.resources.common :as common]
        [cms.resources.collection :as collection])
  (:require [com.ashafa.clutch :as clutch]))

    
(def db2 (clutch/couch "gusto-cms"))
(clutch/create! db2)
(def coll-id (:id (get-collection "test" db2)))
(first (get-collection-items-by-id coll-id db2))
(require 'cms.representations.items)
(require 'cms.representations.collections)
(cms.resources.common/all)
(cms.resources.common/map-from-db (keys db2)
(def test-item (first (get-collection-items "test" db2)))
(:links (cms.representations.collections/links "test"))
(require 'cheshire.core)
(keys (cheshire.core/parse-string (cms.representations.collections/render-collection (get-collection "test" db2)) true))
(cms.representations.items/render-item test-item)
(def test-item-links (:links (cms.representations.items/links test-item)))
(cms.representations.items/-link test-item)
(def raw (keywordize-keys (into {} (map (juxt :_id :data) (map #(get db2 %) (map :id (clutch/get-view db2 "item" :all)))))))
(def filtered (filter #(=  coll-id (-> raw % :collection)) (keys raw)))
(map #(get db2 (name %)) filtered)
(-> raw :b2af465016ab5dcba6fe2f22c100ba39) :collection)
(keys raw)
(def db-resource1 (assoc (cemerick.url/url "http://localhost:5984/" "gusto-cms") :username nil :password nil))
;;(defn db2 [] (clutch/get-database db-resource))
(def db2 (clutch/couch "gusto-cms"))
(clutch/create! db2)
(get-all-documents db2)
(get-collection-items-by-id (:id (get-collection "test2" db2)) db2) 
(get-collection-items "test2" db2)
(get-all-documents db2)
(clutch/all-databases)
(def test-id (:id (get-collection "test" db2)))
(get db2 test-id)
(def ids (map :id  (clutch/get-view db2 "item" :all)))

;; Test Business Domain Entities and Composition Heirarchy
(def user-schema {:properties {:username "foo" :type "password"
                               :passphrase "test123"}
                  :components [:domain :account :group]})

(def domain-schema {:properties {:domain-name "a.com"}
                    :components [:landing-site :market-vector :market-matrix]})

(def landing-site-schema {:properties {:title "Patient Comfort Way"
                                       :html "<html><body>default</body></html>"
                                       :css "div {color: yellow;}"
                                       :snippets []
                                       :tuple-token {(keyword test-xpath)  {:uuid (new-uuid ) :model-uri  "/blah/foo"}
                          :components [:market-vector :market-matrix]})

;; Create the category to store the entity schemas as a collection of items
(create-collection "business-entities")
(def user-schema-item (create-item "business-entities" user-schema))
(def domain-schema-item (create-item "business-entites" domain-schema))
(def landing-site-schema-item (create-item "business-entites" landing-site-schema))

;; Create the categories to host the actual business domain data
(create-collection "user")
(create-collection "domain")
(create-collection "landing-site")
;; Added business domain data
(def user-item (create-item "user" 1 {:username "matt" :passphrase "123test"}))
(def domain-item (create-item "domain" 1 {:domain-name "marketwithgusto.com"}))
(def landing-site-item (create-item "landing-site" 1 {:title "Market With Gusto"}))
;; Get uris to each of the entities
(def user-uri "/user/1")
(def domain-uri "/domain/1")
(def landing-site-uri "/landing-site/1")
;; Create a top level collection to describe an instance of the heirachical data model
;; The collection as set of link pairs
(create-collection "user-profile")

(-> domain-uri (link-to user-uri "user-profile"))
(-> landing-site-uri (link-to domain-uri "user-profile"))
(-> domain-item (add-to user-item "user-profile"))
  

;; The actual linking is a accomplished by adding a link items to the collection
(def user-link {:id 1
                       :parent-type nil
                       :type :user
                       :parent-uri nil
                       :uri  "/user/1"})
(def user-domain-link {:id 1
                       :parent-type :user
                       :type :domain
                       :parent-uri "/user/1"
                       :uri  "/domain/1"})

(def domain-landing-site-link {:id 1
                               :parent-type :domain
                               :type :landing-site
                               :parent-uri "/domain/1"
                               :uri  "/landing-site/1"})

(domain-landing-site-link :uri)
;; Hierarchy is used to describe what children domain objects the item can have links to

(def gather-heirarchy (-> (make-hierarchy)
                           (derive 'cms/user  'user)
                           (derive 'cms/account 'cms/user)
                           (derive 'cms/domain 'cms/user)
                           (derive 'cms/landing-site 'cms/domain)
                           (derive 'cms/market-matrix 'cms/domain)
                           (derive 'cms/market-vector 'cms/market-matrix)
                           (derive 'cms/landing-site 'cms/market-vector)
                           (derive 'advertising/campaign 'cms/market-vector)
                           (derive 'advertising/adgroup 'advertising/campaign)
                           (derive  'advertising/ad 'advertising/adgroup)
                           (derive  'advertising/display-ad  'advertising/ad)
                           (derive  'advertising/text-ad  'advertising/ad)
                           (derive  'advertising/keyword 'advertising/keyword-bucket)
                           (derive  'advertising/keyword-bucket 'advertising/campaign)))

(defn- gather-dispatch [node value]
  (if-let [type (and (= :input (:tag node)) 
                  (-> node :attrs :type))]
    [(keyword (str "input." type)) (class value)]
    [(:tag node) (class value)]))

(defmulti gather
  "Fill a part of the business data module"
  #'gather-dispatch
  :default nil
  :hierarchy #'gather-hierarchy)

(defmethod gather nil
  [collection]
  (if (= :input (:tag node))
    (do
      (alter-var-root #'gather-hierarchy 
        derive (first (gather-dispatch node value)) :input) 
      (gather node value)) 
    (assoc node :content [(str value)]))) 

(defmethod gather
  [:input Object] [node value]
  (assoc-in node [:attrs :value] (str value)))

(defmethod gather [::checkable clojure.lang.IPersistentSet]
  [node value]
  (if (contains? value (-> node :attrs :value))
    (assoc-in node [:attrs :checked] "checked")
    (update-in node [:attrs] dissoc :checked)))



;;(defmethod gather :input

;; Build an instance of the user data that drives their business resources
(create-item "user-profile" user-link)
(create-item "user-profile" user-domain-link)
(create-item "user-profile" domain-landing-site-link)

(def biz-hier (create-business-hierachy "business-entities"))

(def user-data (gather "user-profile" 1))
(def domain-data (:domain user-data))
(def landing-site (:landing-site domain-data))

;; Walk the uri path to create vector to model data
;; Use walk to provide the redirect chain the right progression to get to the desired entity in the hierarchy
;; Validate items to be linked into collection with dynamically created hierarchy using business  entities schema children
;; Link and unlink items into the business domain heirarchy

(def bar (create-item "test" "bar" {:one 1 :two 2}))

(map  #(= ((get db2 %)) ids)
      (filter #(= (:test (get-collections )) (-> (val %) :collection))   (keywordize-keys (into {} (map (juxt :_id :data )
                                                                                                        
                                                                                                        (into {} (get-collection-items-by-id (:id (get-collection "test" db2)) db2))
