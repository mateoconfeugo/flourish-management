(ns cms.site-builder
  "The business logic streams manipulate a composite data structure
   made up of the associated documents, the landing site dom, the
   mapping between the two. "
  (:use [cheshire.core :only [parse-string generate-string]]
        [cms.authoring-utils]
        [clojure.java.io]))

(defn required-fields? [model hierarchy]
  (if  (some #(= false %) (map #(contains? model %) [:layout :xpath :dom]))
    false
    true))

(defn validate [model]
  (assoc model  :status (required-fields? model)))

(defn new-uuid []
  "Generate a unique id"
  (java.util.UUID/randomUUID))

;; TODO: make sure this checks for a pre-existing record
(defn create-xpath-uuid-pairs [model]
  (assoc model :uuid (new-uuid )))

(defn update-landing-site-framework-config [model]
  (assoc model :layout-config 1))

(defn  push-to-editor [model]
  (assoc model :site-html 1))

(defn save-landing-site
  [{:keys [landing-site-id model]}]
  (-> model
      validate
      create-xpath-uuid-pairs
      update-landing-site-framework-config
      ;;        update-base-html
      push-to-editor))

(defn save-landing-site-test
  [xpath]
  (generate-string  {:uuid (new-uuid) :xpath xpath}))

;;  [{:keys [uuid xpath dom layout] :as model}]
(defn update-landing-site
  [{:keys[uuid xpath dom layout domain ls-id cfg] :as model}]
  (generate-string  {:uuid uuid :xpath xpath :dom dom :layout layout}))


(comment
  (defn update-landing-site
    [{:keys[uuid xpath dom layout domain ls-id cfg] :as model}]
    (let [sf (update-snippet model)
          tmpl-path (str (-> cfg :website-dir) "/" domain "/site/landing_site/" ls-id "/cms-resources/views/host_dom.clj")
          fn-ns (str "cms.site-builder." uuid "/render" )
          _ (update-base-template {:selector [(str ".uuid_" uuid)] :snippet-function fs-ns :template-path tmpl-path})
          (load-file (str tmpl-path  "/" uuid "/" uuid ".clj"))]
      (fs-ns  model)))


  (defpipe write-snippet-template []
    "Create the html file that the snippet function will populate
   integrating it into the rest of the page")

  (defpipe build-snippet-function []
    "Take the associated cms document modal data and insert it into snippet html function")

  (defpipe update-base-template []
    "Add, remove, change a selector snippet rule pair")

  (defpipe update-base-html []
    "Add, remove, change the html in the base html template file")

  (defpipe update-component-list []
    "Add, remove, change the html in the base html template file")  

  "Provide the updated html bound to the updated content"

  (defpipeline create-component 
    validate
    write-snippet-template
    build-snippet-function
    update-base-template
    update-base-html
    push-to-editor)

  (defpipeline retrieve-component 
    push-to-editor)

  (defpipeline update-component 
    validate
    push-to-editor)

  (defpipeline delete-component 
    update-base-template
    update-base-html
    push-to-editor)

  (defpipeline list-components 
    update-component-list
    push-to-editor)

  (defpipeline preview-landing-site
    update-component-list
    push-to-editor)


  (defpipeline delete-landing-site
    update-component-list
    push-to-editor)

  (defpipeline edit-landing-site
    update-component-list
    push-to-editor)
  )