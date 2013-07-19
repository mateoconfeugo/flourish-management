(ns test.cms.experiments
  (:import [java.io.FileWriter :as io]))
  (:use [clojure.test :only (is testing deftest)]
        [clojure.java.io]
        [ring.mock.request]
        [cms.site]
	[cms.site-builder]
        [cms.authoring-utils]
        [com.ashafa.clutch :as clutch]        
        [cheshire.core :only (parse-string parse-stream generate-string)]        
        [expectations]
        [fs.core :as fs]
        [net.cgrand.enlive-html :as html]))

;; Values used in tests
;; create database
;; create collection
;; add item to collection

(def cms-test-dir (str (System/getProperty "user.dir") "/test/cms/"))
(def dummy-snippet-path (str cms-test-dir "dummy_snippet.clj"))
(def html-path (str cms-test-dir "dummy-snippet.html"))
(def snippet-output-html-file-path (str cms-test-dir "test-snippet.html"))
(def snippet-output-clojure-file-path (str cms-test-dir "test-snippet-output.clj"))
(def dom "<div id='test-dom'><h1>bar</h1></div>")
(def layout "<html><body><div id='host'>foo</div></body></html>")
(def xpath "/html/body/div/")
(def ls-id 1)
(def base-dir "/tmp/cms/")
(def uuid (new-uuid))
(def test-model {:xpath xpath
                  :dom dom
                  :layout layout
                  :uuid uuid
                 :base-dir base-dir})


(doall
 (take-while
  #(not= ::eof %)
  (repeatedly #(read pbr false ::eof)))))

(def model {:message "bingo"})
(html/html-snippet (slurp html-path))
(html/sniptest (slurp html-path) [:h1] (content (:message model)))
(def snippet-seq (get-snippet-html-seq "gusto-cms" "b2af465016ab5dcba6fe2f22c1097501"))
(def url "http://localhost:5984/gusto-cms/b2af465016ab5dcba6fe2f22c1097501")
(get-snippet-html-reader "http://localhost" 5984  "gusto-cms" "b2af465016ab5dcba6fe2f22c1097501")

(clutch/get-document "gusto-cms" "b2af465016ab5dcba6fe2f22c1097501")

(def ^:dynamic *dummy-select*  [:div])
(html/defsnippet test-dummy buffer *dummy-select*
  [model]
  [:h1] (html/content "huh"))
(test-dummy  model)


(def code-as-string (cheshire.core/generate-string '(html/defsnippet test-dummy1 "templates/dummy-snippet.html" *dummy-select*
  [model]
  [:h1] (html/content "huh"))))

(def test-form '(html/defsnippet test-dummy1 "templates/dummy-snippet.html" *dummy-select*
                  [model]
                  [:h1] (html/content model)))



(defn gunzip
  [fi fo]
  (with-open [i (reader
                 (java.util.zip.GZIPInputStream.
                  (input-stream fi)))
              o (java.io.PrintWriter. (writer fo))]
    (doseq [l (line-seq i)]
      (.println o l))))


;;(def documents-db (str couchdb "/documents/"))
(require 'fs.core)
(import 'java.io.FileWriter)
(def test-doc-pdf-path "/Users/matthewburns/bin/incanter/pdf-matrix.pdf")
(def test-doc-pdf {:filename test-doc-pdf-path
                   :mime-type "application/pdf"
                   :data-length (fs.core/size test-doc-pdf-path)
                   :data (->> test-doc-pdf-path
                              fs.core/absolute-path
                              fs.core/file
                              clojure.java.io/input-stream)})
  ;;  (let [couchdb-args (concat [db { :_id (str (d/squuid))} (:data doc)]

(add test-doc-pdf)


(defn save-code
  [data-buffer form]
  (with-open [output (java.io.BufferedOutputStream.
                   (java.io.BufferedInputStream. data-buffer))]
    (binding [data-buffer output *print-dup* true] (prn  form))))

(defn load-code
  [data-buffer]
  (with-open [buffer (java.io.BufferedReader.
                   (java.io.InputStreamReader. data-buffer))]
    (apply str (line-seq buffer))))

(require 'clojure.string)

(defn gunzip-text-lines
  "Returns the contents of input as a sequence of lines (strings).
  input: something which can be opened by io/input-stream.
      The bytes supplied by the resulting stream must be gzip compressed.
  opts: as understood by clojure.core/slurp pass :encoding \"XYZ\" to
      set the encoding of the decompressed bytes. UTF-8 is assumed if
      encoding is not specified."
  [input & opts]
  (with-open [input (-> input input-stream java.util.zip.GZIPInputStream.)]
    (clojure.string/split-lines (apply slurp input opts))))

(defn gunzip
  "Writes the contents of input to output, decompressed.

  input: something which can be opened by io/input-stream.
      The bytes supplied by the resulting stream must be gzip compressed.
  output: something which can be copied to by io/copy."
  [input output & opts]
  (with-open [input (-> input input-stream java.util.zip.GZIPInputStream.)]
        (apply copy input output opts)))

(use '[com.ashafa.clutch.http-client :only (*response*)])

(def foo (file "/tmp/foo.clj"))
(def bar (file "/tmp/bar.clj"))
(save-s-expression foo test-form)
(eval (load-s-expression foo))
(test-dummy1 "blah")
(def test-doc (clutch/get-document "gusto-cms" "b2af465016ab5dcba6fe2f22c105fe62"))
;;(def test-doc (clutch/get-document "gusto-cms" "b2af465016ab5dcba6fe2f22c1060621"))
(clutch/put-attachment "gusto-cms" test-doc foo :mime-type "text/plain" )
(def test-attach (clutch/get-attachment "gusto-cms" test-doc  "foo.clj"))

(def the-doc (binding [*response* nil]
  (let [attachment (get-attachment "gusto-cms" test-doc  "foo.clj")]
    attachment (-> *response* ))))

(keys (:body the-doc)
  

(binding [*response* nil]
  (let [attachment (get-attachment "gusto-cms" test-doc  "foo.clj")]
    attachment (-> *response* :headers (select-keys ["content-length" "content-type" "text/plain"])))))



(keys the-doc)



(reader (java.util.zip.GZIPInputStream.  (input-stream test-attach)))
(gunzip test-attach bar)




(def foo (doseq [f (read-string "(def bar 2)")]))
  (eval foo)

(binding [*print-dup* true] (println [1 2 3]))

(pprint (read-string "(def foo 1)"))
(load-string "(def foo 1)")


(def code (parse-string code-as-string))
(def code (reverse (into () code)))
(cons code (into () (last  code)))

(test-dummy1  model)

(def ^:dynamic *dummy-select*  [:div])
(html/defsnippet test-dummy2  snippet-seq *dummy-select*
  [model]
  [:h1] (html/content model))

(test-dummy2 "bingo")

     

(deftest sb-read-all-test
  (let [code-as-data (read-all dummy-snippet-path)
        (load-file dummy-snippet-path))]
    (is 1 1)))

(deftest sb-update-base-template-test
  (let [_ (update-base-template test-model)]
    (exist? (as-file snippet-output-clojure-file-path))))

(deftest sb-read-snippet-code-file-test
  (let [code-as-data (read-snippet-code snippet-clojure-dummy-code-file-path)]
    (= 1 1)))

(deftest sb-write-snippet-code-file-test
  (let [code-as-data (write-snippet-code-file snippet-clojure-dummy-code-file-path)]
    (= 1 1)))

(deftest sb-write-snippet-code-test
  (let [code-as-data (write-snippet-code snippet-clojure-dummy-code-file-path)]
    (= 1 1)))

(deftest sb-write-snippet-html-test
  (let [resulting-model (write-snippet-html model)]
    (exist? (as-file snippet-output-html-file-path))))

(deftest sb-update-snippet-test
  (let [_ (update-snippet test-model)]
    (exist? (as-file snippet-output-clojure-file-path))))

(deftest sb-process-element-test
  (let [fields (process-element test-element)]
    (is (= (:uuid fields) test-uuid))))

(deftest sb-field-item-model-snippet-test
  (let [html (field-item-model  field-item-model-data)]
    (is (= html field-item-model-test-html))))

(deftest sb-update-landing-site-test
  (let [json (update-landing-site landing-site-model)]
    (is (= json landing-site-response))))

(expect true (= 1 sb-read-all-test))
(expect true (= 1 sb-update-base-template-test))        
(expect true (= 1 sb-read-snippet-code-file-test))
(expect true (= 1 sb-write-snippet-code-file-test))
(expect true (= 1 sb-write-snippet-code-test))
(expect true (= 1 sb-write-snippet-html-test))
(expect true (= 1 sb-update-snippet-test))
(expect true (= 1 sb-process-element-test))
(expect true (= 1 sb-field-item-model-snippet-test))
(expect true (= 1 sb-update-landing-site-test))

(defmacro get-snippets
  "Expands to a map of HTML snippets"
  []
  (merge
   (read-snippets "feed"
                  "templates/cms/feed-overview.html"
                  [:.feed-list-for-language-container
                   :.feed-list-row
                   :add-feed-button
                   :add-document-button
                   :document-list
                   :.document-list-row
                   :add-document
                   :feed-name-row
                   :feed-title-row
                   :feed-subtitle-row
                   :feed-default-slug-format-row
                   :feed-custom-slug-format-row
                   :feed-default-document-type-row
                   :feed-searchable-row
                   :feed-language-row
                   :save-feed])
   (read-snippets "ui"
                  "templates/cljs/ui.html"
                  [:status-message
                   :caption
                   :date-widget
                   :datepicker-time-row])
   (read-snippets "editor"
                  "templates/cljs/editor.html"
                  [:back-to-overview-link
                   :image-drop-target
                   :image-information-container
                   :title-row
                   :subtitle-row
                   :slug-row
                   :start-time-row
                   :end-time-row
                   :icon-container
                   :document-relations
                   :image-relations
                   :editor-images
                   :description-container
                   :content-container
                   :save-button-container
                   :.feed-select-option
                   :.document-select-option
                   :.related-page
                   :.related-image
                   :add-related-page-dialog-form
                   :add-image-dialog-form
                   :.image-icon-container
                   :image-preview-in-dialog-container
                   :menu-builder
                   :.top-level-menu-item
                   :.nested-menu-item
                   :.nested-menu-category
                   :.item-details
                   :.add-item-node
                   :.add-sub-item
                   :add-menu-item-container
                   :menu-instructional-paragraph
                   :add-menu-item-dialog-form])))


