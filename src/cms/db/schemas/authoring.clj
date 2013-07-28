(ns cms.db.schemas.authoring
  (:use [cms.authoring-utils :only [new-uuid]]))

(def dom "<div id='test-dom'>bar</div>")
(def layout "<html><body><div id='host'>foo</div></body></html>")
(def xpath "/html/body/div")
(def ls-id 1)
(def base-dir "/tmp/cms/")
(def uuid (new-uuid))


;; Hierarchy is used to describe what children domain objects the item can have links to
(def authoring-heirarchy (-> (make-hierarchy)
                           (derive :account :user)
                           (derive :domain :user)
                           (derive :market-matrix :domain)
                           (derive :market-vector :market-matrix)
                           (derive :landing-site :market-vector)
                           (derive :landing-site :website)
                           (derive :document :landing-site)
                           (derive :ad-campaign :market-vector)
                           (derive :ad-group :ad-campaign)))

(def default-model {:xpath xpath
                 :dom dom
                 :layout layout
                 :uuid uuid
                 :base-dir base-dir})

(def user-schema {:entity-type :user
                  :properties   {:username {:value "foo" :type "text" :max 1 :min 1 :length 50}
                                 :passphrase {:value "test123" :type "password" :max 1 :min 1}}
                  :components {:domain {}
                               :account {:min 1}
                               :group {:min 1}}})

(def domain-schema {:entity-type :domain
                    :properties {:domain-name {:value "a.com" :type "text" :max 1 :min 1}}
                    :components {:landing-site {:min 1}
                                 :market-vector {:min 1} 
                                 :market-matrix {:min 1}}})

(def xpath-uuid-uri-tuple-schema {:entity-type :xpath-uuid-uri-tuple
                                  :properties {:xpath (:xpath default-model)
                                               :uuid (new-uuid )
                                               :uri  "/blah/foo"}})

(def locator-index-schema {:entity-type :locator-index
                           :components [:xpath-uuid-uri-tuple]})

(def landing-site-schema {:entity-type :landing-site
                          :properties {:title {:value "Patient Comfort Way" :max 1 :min 1}
                                       :html {:value (:layout default-model)  :min 1}
                                       :css  {:value (:css default-model)  :min 1}
                                       :snippets {:value []}}
                          :components [:locator-index]})

(def entity-link-schema {:entity-type :entity-link
                         :properties {:parent-type {:value :user :type "keyword"}
                                      :parent-uri {:value "/user/1" :type "text" }
                                      :type {:value :domain :type "keyword"}
                                      :uri {:value "/domain/1" :type "text"}}})
