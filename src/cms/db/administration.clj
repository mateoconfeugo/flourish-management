(ns cms.db.administration
  (:refer-clojure :exclude [assoc! conj! dissoc! name parents replace reverse])    
  (:use [cms.db.schemas.authoring]
        [cms.db.views :only [init-db]]        
        [cms.resources.item :only [create-item]]
        [cms.resources.common :as common]
        [cms.resources.collection :as collection :only [create-collection]]))

(defn initialize-authoring-data-layout
  [db]
  "Given an empty database create all the necessary authoring entity schemas along
   with the interface the application expects"
  (let [entities (collection/create-collection db "business-entities")
        models (collection/create-collection db "business-models")]
  {:business-entities entities
   :business-models models
   :user-schema-item (create-item db "business-entities"  user-schema)
   :domain-schema-item (create-item db "business-entities" domain-schema)
   :landing-site-schema-item (create-item db "business-entities" landing-site-schema)
   :entity-link-schema-item (create-item db "business-entities" entity-link-schema)
   :xpath-uuid-uri-tuple-schema (create-item db "business-entities" xpath-uuid-uri-tuple-schema)
   :locator-schema (create-item db "business-entities" locator-index-schema)
   :user-collection (create-collection db "user")
   :domain-collection (create-collection db "domain")
   :landing-site-collection (create-collection db "landing-site")}))

(defn setup-user-data-resources
  [username  design-mode]
  "Create the database populated with the data need to get a new user operational"
  (let [db (cms.db.views/init-db username design-mode)
        entities (initialize-authoring-data-layout db)]
    entities))

;; TODO:   Check if username already exists
(defn add-new-authoring-user
  [{:keys [db user domain landing-site] :as args}]
  "Create the entity instances a user needs to author content
   and link them together into the business model needed to
   publish a landing site"
  (let [user-item (create-item db "user" (:username user) user)
        domain-item (create-item  db  "domain" (:name domain) domain)
        landing-site-item (create-item db "landing-site" (:id landing-site) landing-site)
        links {:user (str "/user/" (:username user))
               :domain (str "/domain/" (:name domain))
               :landing-site (str "/landing-site/" (:id landing-site))}
        user-link {:id (:username user)
                :parent-type nil
                :type :user
                :parent-uri nil
                :uri  (:user links)}
        user-domain-link {:id (:name domain)
                       :parent-type :user
                       :type :domain
                      :parent-uri (:user links)
                       :uri  (:domain links)}
        domain-landing-site-link {:id (:id landing-site)
                               :parent-type :domain
                               :type :landing-site
                               :parent-uri (:domain links)
                               :uri  (:landing-site links)}
        authoring-profile-model (create-item db "business-models" "authoring-profile" {})
        authoring-profile-coll (collection/create-collection db "authoring-profile")
        authoring-profile (create-item db "authoring-profile"(:username user) {:entity-links [user-link user-domain-link domain-landing-site-link]})]
    authoring-profile))

(comment
(-> domain-uri (link-to user-uri "user-profile"))
(-> landing-site-uri (link-to domain-uri "user-profile"))
(-> domain-item (add-to user-item "user-profile"))
)
