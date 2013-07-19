(ns cms.authoring-utils
  "Functions and macros that help generate the clojure code"
  (:import [org.apache.commons.codec.binary Base64])
  (:require [fs.core :as fs :exclude [copy file]])
  (:use [cheshire.core :only [parse-string generate-string]]
        [clojure.pprint]
        [clojure.java.io]
        [cms.site]
        [data-formating :only [decode-str]]
        [com.ashafa.clutch :as clutch :only(get-document put-attachment)]                
        [net.cgrand.enlive-html :as html]))

(defn update-snippet-form
  "Updates or creates a snippet corresponding using the correct html"
  [{:keys [uuid tmpl-path form selector rule] :as args}]
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
  "Save a clojure form to file."
  [#^java.io.File file form]
  (with-open [w (java.io.FileWriter. file)]
    (binding [*out* w *print-dup* true]  (prn  form))))

(defn load-s-expression
  "Load a clojure form from file."
  [#^java.io.File file]
  (with-open [r (java.io.PushbackReader.
                 (java.io.FileReader. file))]
    (let [rec (read r)]
      rec)))

(defn attach-snippet
  [{:keys [db doc uuid file-path mime-type] :as args}]
  (let [doc-id (:_id doc)
        attachment-file-path (or file-path (str "/" "tmp/" "snip_attachment_" uuid ".clj"))
        file (->> attachment-file-path
                  fs.core/absolute-path
                  fs.core/file)
        data (clojure.java.io/input-stream file)
        dl (fs.core/size file)  
        mime-type (or mime-type "text/plain")]
    (clutch/put-attachment db doc data :filename attachment-file-path :mime-type mime-type :data-length dl)))

(defn get-snippet-attachment
  "Returns string or nil (if the attachment wasn't
   found) for the attachment with the given attachment-key in
   document-id and database."
  [database document-id attachment-key]
  (when-let [a (clutch/get-attachment database document-id attachment-key)]
    (let [byte-array-os (java.io.ByteArrayOutputStream.)]
      (copy a byte-array-os)
      (decode-str (Base64/encodeBase64String (.toByteArray byte-array-os))))))

(defn load-snippet-form
  [{:keys [db doc uuid] :as args}]
  (let [tmp-file (str "/tmp/snippet_attachment_"  uuid ".clj")
        code-file (str "/tmp/snippet_attachment_"  uuid ".retreived.clj")
        attachment (get-snippet-attachment db (:_id doc) tmp-file)
        _ (spit code-file attachment :append true)]       
    (load-s-expression (file code-file))))

(defn save-snippet-form
  [{:keys [db doc uuid form] :as args}]
  (let [tmp-file-path (str "/tmp/snippet_attachment_"  uuid ".clj")
        _ (spit tmp-file-path form :append true)
        tmp-file (fs.core/file tmp-file-path)
        _ (save-s-expression tmp-file form)]
    (attach-snippet (merge args {:file-path tmp-file-path}))))

(defn save-snippet-html
  [db doc-id uuid html]
  "Generate the html file that the snippet uses"
  (update-in db doc-id html))

(defn get-snippet-html
  [{:keys [doc-id db] :as args}]
  (:html (get db doc-id)))

(defn save-snippet-css
  [{:keys [db doc-id uuid css] :as args}]
  "Generate the html file that the snippet uses"
  (update-in db doc-id css))

(defn get-snippet-css
  [{:keys [doc-id db] :as args}]
  (:css (get db doc-id)))

(defn load-snippet
  [{:keys [db doc uuid] :as args}]
  (let [form (load-snippet-form args)
        _ (eval form)]
    {:form form
     :html (get-snippet-html args)
     :css (get-snippet-css args)}))

(defn save-snippet
  [{:keys [db doc dom selector form rule uuid html css] :as args}]
  (do
    (clutch/update-document db doc {:css  css
                                    :html html})
    (save-snippet-form {:db db :doc doc :uuid uuid :form form})))

(defn read-all
  [input]
  (let [eof (Object.)]
    (take-while #(not= % eof) (repeatedly #(read input false eof)))))

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

(defn publish-snippet-html
  [dir uuid html]
  (with-open [w (writer (str dir "/" uuid "/" uuid ".html"))]
    (.write w html)))

(defn publish-snippet-clj
  [dir uuid form]
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
