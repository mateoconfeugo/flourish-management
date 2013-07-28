(ns cms.document
  "Test Business Domain Entities and Composition Heirarchy of
   the operations around the landing site"
  (:require [clojure.string :as str]
            [clojure.java.io :as io]
            [com.ashafa.clutch :as clutch])
  (:use [clojure.test :only (is testing deftest)]
        [clojure.walk]
        [clojure.core]
        [cms.authoring-utils :only [new-uuid]]
        [cms.db.views]
        [cms.document]
        [cms.resources.item]
        [cms.resources.common :as common]
        [cms.resources.collection :as collection]        
        [cheshire.core :only (parse-string parse-stream)]        
        [expectations]))

;; Input test values normally provided by the rest resource hook
(def dom "<div id='test-dom'>bar</div>")
(def layout "<html><body><div id='host'>foo</div></body></html>")
(def xpath "/html/body/div")
(def ls-id 1)
(def base-dir "/tmp/cms/")
(def uuid (new-uuid))
(def test-model {:xpath xpath
                 :dom dom
                 :layout layout
                 :uuid uuid
                 :base-dir base-dir})

;; We are going to need a user domain and a landing site so ...
;; TEST: Creating a user database in document store for customized authored content and settings
(cms.db.views/init-db "alpha")
(expect true (= "gusto-cms-alpha" (:db_name (clutch/database-info (common/get-user-database "alpha")))))

;; Add instances of business entities items to their respective collections

;; Get uris to each of the entities


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



