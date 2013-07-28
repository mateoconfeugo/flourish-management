(ns cms.authoring-utils
  "Functions and macros that help generate the clojure code
   and storing and retreiving code from the document store"
  (:refer-clojure :exclude [assoc! conj! dissoc! name parents])
  (:require [clojure.core :as core]) 
  (:import [org.apache.commons.codec.binary Base64])
  (:use [cheshire.core :only [parse-string generate-string]]
        [clojure.pprint]
        [clojure.java.io]
        [cms.site]
        [com.ashafa.clutch :as clutch :only(get-document put-attachment database-info)]
        [data-formating :only [decode-str]]
        [me.raynes.fs :as fs :exclude [copy file]]                
        [net.cgrand.enlive-html :as html]
        [validation]))

;(defn add-revision [rev-id]
;  (let [uniq? (every? #(false? (= % rev-id)) @*revisions*)]
;    (if uniq? (do (swap! *revisions* conj rev-id) true) false)))


(defn new-uuid []
  "Generate a unique id"
  (.toString (java.util.UUID/randomUUID)))

(defn in?
  "true if seq contains elm"
  [seq elm]
  (some #(= elm %) seq))

(defn read-all
  [f]
  "Reads all top-level forms from f, which will be coerced by
  clojure.java.io/reader into a suitable input source. Not lazy."
  (with-open [pbr (java.io.PushbackReader. (clojure.java.io/reader f))]
    (doall
     (take-while
      #(not= ::eof %)
            (repeatedly #(read pbr false ::eof))))))

(defn update-snippet-form
  [{:keys [uuid tmpl-path form selector rule] :as args}]
  "Updates or creates a snippet corresponding using the correct html"  
  (let [form-tmpl (or form
                      '(html/defsnippet snippet-name tmpl-path selector [model] [:div] (html/content model)))
        new-form (-> (into [] form-tmpl)            
                     (assoc-in [1] (symbol (str "snippet-" uuid)))
                     (assoc-in [2] tmpl-path)
                     (assoc-in [3] selector)
                     (assoc-in [5] (first rule))
                     (assoc-in [6] (last rule)))]
    (reverse (into () new-form))))

(defn save-s-expression
  [#^java.io.File file form]
  "Save a clojure form to file."  
  (with-open [w (java.io.FileWriter. file)]
    (binding [*out* w *print-dup* true]  (prn  form)))
  file)

(defn load-s-expression
  [#^java.io.File file]
  "Load a clojure form from file."
  (first (read-all file)))

(defn attach-code-file
  [{:keys [db doc-or-id file attachment-key mime-type] :as args}]
  "Add the clojure source code file as stringified form to the snippet document"
;  (validate [db :exists?] [file :exists?] [attachment-key :exists?])
  (let [doc (if (map? doc-or-id) (clutch/get-document db (:_id doc-or-id)) (clutch/get-document db doc-or-id))
        doc-id (:_id doc)
        data (clojure.java.io/input-stream file)
        dl (fs.core/size file)  
        mime-type (or mime-type "text/plain")]
    (clutch/put-attachment db doc data :filename attachment-key :mime-type mime-type :data-length dl)))

(defn get-attached-code-file
  [db doc-or-id attachment-key]  
  "Returns string or nil for the attachment with the given attachment-key
   data is stream in to a byte array which is transformed to its format
   then decoded"
  (let [doc (if (map? doc-or-id) doc-or-id (clutch/get-document db doc-or-id))]
    (when-let [attached (clutch/get-attachment db (:_id doc) attachment-key)]
      (let [byte-array-os (java.io.ByteArrayOutputStream.)]
        (copy attached byte-array-os)
        (decode-str (Base64/encodeBase64String (.toByteArray byte-array-os)))))))

(defn load-template-form 
  [{:keys [db doc-or-id] :as args}]
  "Get the clojure enlive template code attached to the document"
  (let [db-name (:db_name db)
        doc (if (map? doc-or-id) (clutch/get-document db-name (:_id doc-or-id)) (clutch/get-document db doc-or-id))
        attachment-key (str "enlive_template.clj")
        attachment (get-attached-code-file db  doc attachment-key)
        tmp-name (fs/temp-name "enlive_template")
        abs-path (fs/absolute-path tmp-name)
        _ (spit abs-path attachment)]
    (load-s-expression (fs/file  abs-path))))

(defn load-snippet-form
  [{:keys [db doc-or-id uuid] :as args}]
  "Get the clojure snippet code attached to the document"
  (let [db-name (:db_name db)
        doc (if (map? doc-or-id) (clutch/get-document db-name (:_id doc-or-id)) (clutch/get-document db doc-or-id))
        attachment-key (str "snippet_"  uuid ".clj")
        attachment (get-attached-code-file db  doc attachment-key)
        tmp-name (fs/temp-name "snippet")
        abs-path (fs/absolute-path tmp-name)
        _ (spit abs-path attachment)]
    (load-s-expression (fs/file  abs-path))))

(defn save-template-form 
  [{:keys [db doc-or-id form] :as args}]
  "Save the clojure enlive template code as an attachment to the document"  
  (let [doc (if (map? doc-or-id) (clutch/get-document db (:_ids doc-or-id)) (clutch/get-document db doc-or-id))
        ak (str "enlive_template.clj")
        tf (fs/temp-file "enlive_template")]
    (try
      (clutch/delete-attachment (:db-name db) doc ak)
      (catch Exception e "EXCEPTION" e {:result :conflict}))
    (attach-code-file (merge args {:attachment-key ak :db db :file (save-s-expression tf form)}))))

(defn save-snippet-form
  [{:keys [db doc-or-id uuid form] :as args}]
  "Save the clojure snippet code as an attachment to the document"  
  (let [doc (if (map? doc-or-id) (clutch/get-document db (:_ids doc-or-id)) (clutch/get-document db doc-or-id))
        ak (str "snippet_"  uuid ".clj")
        tf (fs/temp-file "snippet")]
    (try
      (clutch/delete-attachment (:db-name db) doc ak)
      (catch Exception e "EXCEPTION" e {:result :conflict}))
    (attach-code-file (merge args {:attachment-key ak :db db :file (save-s-expression tf form)}))))

(defn save-snippet-html
  [db doc-or-id html]
  "Generate the html file that the snippet uses"
  (let [doc (if (map? doc-or-id) (clutch/get-document db (:_id doc-or-id)) (clutch/get-document db doc-or-id))]
    (clutch/update-document db doc {:html html})))

(defn get-snippet-html
  [db doc-or-id]
  (if (map? doc-or-id)
    (:html (clutch/get-document db (:_id doc-or-id)))    
    (:html (get db doc-or-id))))

(defn save-snippet-css
  [db doc-or-id css]
  "save the css file that the snippet uses"
  (let [doc (if (map? doc-or-id) (clutch/get-document db (:_id doc-or-id)) (clutch/get-document db doc-or-id))]
    (clutch/update-document db doc {:css css})))

(defn get-snippet-css
  [db doc-or-id]
  (if (map? doc-or-id)
    (:css (clutch/get-document db (:_id doc-or-id)))    
    (:css (get db doc-or-id))))

(defn load-snippet
  [{:keys [db doc-or-id uuid] :as args}]
  "Return a map of the css, html, and code for a snippet from the document store"
  (let [doc (if (map? doc-or-id) (clutch/get-document db (:_id doc-or-id)) (clutch/get-document db doc-or-id))
        form (load-snippet-form {:db db :doc-or-id (:_id doc) :uuid uuid})
        _ (eval form)]
    {:form form
     :html (:html doc)
     :css (:css doc)}))

(defn save-snippet
  [{:keys [db doc-or-id dom selector form rule uuid html css] :as args}]
  "Save the css, html, and code for a snippet to the document store"  
  (let [doc (if (map? doc-or-id) (clutch/get-document db (:_id doc-or-id)) (clutch/get-document db doc-or-id))
        updated (clutch/with-db (:db_name (database-info db))
                  (clutch/update-document doc {:css css :html html}))
        doc (clutch/get-document db (:_id doc-or-id))        
        _ (save-snippet-form {:db db :doc-or-id doc :uuid uuid :form form})
        _ (save-snippet-html db doc html)
        _ (save-snippet-css db doc css)]
    doc))

(defn create-template-form
  [{:keys [template-path output rules namespace] :as model}]
  "Takes an existing clojure source code file changes
   the deftemplate macro rules so that at runtime
   a customized function that renders the landing site
   and binds in the content to the dom can be called to
   service requests"
  (let [tsf (read-all template-path)
        ns-macro (reverse (into () (assoc (into [] (first tsf)) 1 namespace)))
        tc (nth tsf 1)
        macro-type (nth tc 0)
        macro-name (nth tc 1)
        macro-tmpl (nth tc 2)
        macro-args (nth tc 3)
        len (count tc)
        amount (- len 4)
        pairs (take-last amount tc)
        existing-rules (partition 2 pairs)]
    (with-open [w (writer output)]
      (.write w (str ns-macro))
      (.write w (str "(" macro-type " "  macro-name "  \""  macro-tmpl "\" "  macro-args ))
      (doseq [r rules]
        (.write w (str "   [" (nth r 0) "]  (" (nth r 1) " " (nth r 2)")")))
      (.write w ")"))
    (do
      (read-all (fs/file output)))))

(defmacro template-ns [tmpl-form]
  "Creates the namespace and dependencies using the ns macro"
  `(let [top# (rest (nth ~tmpl-form 0))
         name# (nth top# 0)
         ns# (rest top#)
         tc# (nth ~tmpl-form 1)
         ns0# (nth ns# 0)
         ns1# (nth ns# 1)
         ns2# (nth ns# 2)]
     (str (list 'ns name#  ns0# ns1# ns2#))))

(defmacro define-template-macro [tmpl-form]
   "Needed so I could break up the transformation steps"
  `(nth ~tmpl-form 1))

(defn create-view-template-file
  [{:keys [tmpl-form rules output-file naming-scheme]}]
  "Creates a file of an enlive clojure view ready to be included/required used in a module"
  (let [tmp-file-name (or output-file (fs/temp-name (or naming-scheme "enlive_template")))
        tmp-file (fs/file tmp-file-name)
        name-space (template-ns tmpl-form)
        template-macro (define-template-macro tmpl-form)]
    (do
      (spit tmp-file name-space)
      (doseq [r rules] (spit tmp-file r :append true))
      (spit tmp-file  template-macro :append true)
      tmp-file)))
  
(defn publish-snippet-html
  [dir uuid html]
  "Move the html from the snippet document in the database  into a file "
  (with-open [w (writer (str dir "/" uuid "/" uuid ".html"))]
    (.write w html)))

(defn publish-snippet-css
  [dir uuid css]
  "Move the html from the snippet document in the database  into a file "
  (with-open [w (writer (str dir "/" uuid "/" uuid ".css"))]
    (.write w html)))

(defn publish-snippet-clj
  [dir uuid form]
  "Move the html from the snippet document in the database  into a file"  
  (save-s-expression (java.io.File. (str dir "/" uuid "/" uuid ".clj") form)))

(defn read-snippet
  "Extracts the snippet with given identifier from the provided enlive
   resource and returns it as a string. If the identifier starts with
   a dot it is used as-is (as a class value), otherwise it is assumed
   to be an id attribute value and prepended by a #-character."
  [resource identifier]
  (apply str
         (html/emit*
          (html/select resource
                       [(let [name- (name identifier)]
                          (if (= (first name- ) \.)
                            identifier
                            (keyword (str "#" name-))))]))))

(defn read-snippets
  "Returns a map with the extract snippets from the provided filename
   using the given ids sequence as snippet id values, prefixed by the
   provided ns (resulting in keys like :feed/add-feed-button)."
  [ns filename ids]
  (zipmap (map (fn [id] (keyword (str ns "/" (name id)))) ids)
          (map (partial read-snippet (html/html-resource filename)) ids)))
