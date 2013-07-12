(ns cms.site-builder
  "The business logic streams manipulate a composite data structure
   made up of the associated documents, the landing site dom, the
   mapping between the two. "
  (:use [flourish-common.pipeline :only[defpipe defpipeline return]]
        [cheshire.core :only [parse-string generate-string]]
        [clojure.pprint]
        [clojure.java.io]
        [cms.site]))

(defn read-all
  [f]
  "Reads all top-level forms from f, which will be coerced by
  clojure.java.io/reader into a suitable input source. Not lazy."  
  (with-open [pbr (java.io.PushbackReader. (clojure.java.io/reader f))]
    (doall
     (take-while
      #(not= ::eof %)
      (repeatedly #(read pbr false ::eof))))))

(defn update-base-template
  [{:keys [template-path selector snippet-function output] :as model}]
  "Takes an existing clojure source code file changes
   the deftemplate macro rules so that at runtime
   a customized function that renders the landing site
   and binds in the content to the dom can be called to
   service requests"
  (let [tsf (read-all template-path)
        ns-macro (first tsf)
        tc (nth tsf 2)
        macro-type (nth tc 0)
        macro-name (nth tc 1)
        macro-tmpl (nth tc 2)
        macro-args (nth tc 3)
        len (count tc)
        amount (- len 4)
        pairs (take-last amount tc)
        rules (partition 2 pairs)]
    (with-open [w (writer "/tmp/foo.clj")]
      (.write w (str (first tsf) "\n\n\n"))
      (.write w (str "(" macro-type " "  macro-name " \""  macro-tmpl "\"\n   "  macro-args "\n"))
      (doseq [r rules] 
        (.write w (str "   " (first r) " " (last r) "\n")))
      (.write w (str "   " selector " " snippet-function))
      (.write w ")\n"))
    model))

;;  [{:keys [uuid xpath dom layout] :as model}]
(defn update-landing-site
  [{:keys[uuid xpath dom layout domain ls-id cfg] :as model}]
  (generate-string  {:uuid uuid :xpath xpath :dom dom :layout layout}))



(comment

  (defn read-snippet-code-file
    [{:keys [tmpl-path] :as model}]
    "Create a new snippet clojure file from a dummy template, create a
   new snippet html file from the dom and update the landing
   site config json file"
    (let [snippet-code (read-all (str base-dir "/" tmpl-path))
          ns-macro (first snippet-code)
          selector ""
          sc (nth snippet-code 2)]
      {:macro-type (nth sc 0)
       :macro-name (nth sc 1)
       :macro-tmpl (nth sc 2)
       :macro-args (nth sc 3)
       :len (count sc)
       :amount (- len 4)
       :pairs (take-last amount sc)
       :rules (partition 2 pairs)}))

  (defn write-snippet-code-file
    [{:keys [tmpl-path macro-type macro-name macro-tmpl macro-args] :as model}]
    "Create a well formed compiling working clojure source file
   that contains the defsnippet"
    (with-open [w (writer tmpl-path)]
      (.write w (str (first tsf) "\n\n\n"))
      (.write w (str "(" macro-type " "  macro-name " \""  macro-tmpl "\"\n   "  macro-args "\n"))
      (doseq [r rules] 
        (.write w (str "   " (first r) " " (last r) "\n")))
      (.write w (str "   " selector " " snippet-function))
      (.write w ")\n"))
    model)

  (defn write-snippet-code
    [{:keys [base-dir landing-site-cfg] :as model}]
    "Create a new snippet clojure file from a dummy template, create a
   new snippet html file from the dom and update the landing
   site config json file"
    (let [snippet-parts (read-snippet-code-file dummy-template-path)
          _ (write-snippet-code-file snippet-parts)]
      model))

  (defn write-snippet-html
    [{:keys [base-dir uuid dom] :as model}]
    "Generate the html file that the snippet uses"
    (do
      (with-open [w (writer (str base-dir "/" uuid "/" uuid ".html"))]
        (.write w dom))
      model))

  (defn update-snippet
    [{:keys [uuid dom xpath ls-id base-dir] :as model }]
    "Updates or creates the html and clojure files that a snippet uses"
    (let [base-dir ""
          ls-cfg (landing-site-json base-dir ls-id)
          clj-code-path (str base-dir "/" uuid "/" uuid ".clj")]
      (if (contains? (-> ls-cfg :snippet-mapping :uuid))
        (->> (assoc model :tmpl-path clj-code-path read-snippet-code write-snippet-code write-snippet-html)
             (-> (assoc model :tmpl-path (str base-dir  "/" uuid "/" uuid ".html") :landing-site-cfg  ls-config)
                 read-snippet-code
                 write-snippet-html)))
      (load-file clj-code-path)))

  (defn process-elements
    [elements]
    "Recursively builds up dom rendered cms heirarctical document"
    (doseq [element elements]
      (if (contains? element :elements)
        (process-elements (:elements element))
        (let [binding (get-dom-binding (:id element))
              uuid (:uuid binding)]
          (uuid (:fields element))))))

  (defsnippet field-item-model "templates/field-item.html" *field-item-sel*
    [{:keys [item-key item-value] :as field}]
    [(str "#" item-key)] (content item-value))


  (defn update-landing-site
    [{:keys[uuid xpath dom layout domain ls-id cfg] :as model}]
    (let [sf (update-snippet model)
          tmpl-path (str (-> cfg :website-dir) "/" domain "/site/landing_site/" ls-id "/cms-resources/views/host_dom.clj")
          fn-ns (str "cms.site-builder." uuid "/render" )
          _ (update-base-template {:selector [(str ".uuid_" uuid)] :snippet-function fs-ns :template-path tmpl-path})
          (load-file (str tmpl-path  "/" uuid "/" uuid ".clj"))]
      (fs-ns  model)))



  (defn required-fields? [model]
    (if  (some #(= false %) (map #(contains? model %) [:layout :xpath :dom]))
      false
      true))

  (defn validate [model]
    (assoc model  :status (required-fields? model)))


  (defn write-snippet-template [model]
    (assoc model :snippet-html 1))

  (defn build-snippet-function [model]
    (assoc model :snippet-code 1))

  (defn update-base-html [model]
    "Write out the html file with the uuids"
    (assoc model :template-html 1))

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
        update-base-html
        push-to-editor))

  (defn save-landing-site-test
    [xpath]
    (generate-string  {:uuid (new-uuid) :xpath xpath}))

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