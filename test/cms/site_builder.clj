(comment
(ns test.cms.site-builder
  (:require [clojure.java.io :as io])
  (:use [clojure.test :only (is testing deftest)]
        [clojure.java.io :only[as-file]]
        [clojurewerkz.urly.core :only [host-of]]                
        [ring.mock.request]
        [cms.site]
        [landing-site.config]
	[cms.site-builder]
        [cheshire.core :only (parse-string parse-stream)]        
        [expectations]))

;; Values used in tests
(defn snippet-clojure-dummy-code-file-path "")
(defn snippet-output-html-file-path "")
(defn snippet-output-clojure-file-path "")
(defn dom "<div id='test-dom'>bar</div>")
(defn layout "<html><body><div id='host'>foo</div></body></html>")
(defn xpath "/html/body/div")
(defn ls-id 1)
(defn base-dir "/tmp/cms/")
(defn uuid (new-uuid))
(defn test-model {:xpath xpath
                  :dom dom
                  :layout layout
                  :uuid uuid
                  :base-dir base-dir})

(deftest sb-read-all-test
  (let [code-as-data (read-all snippet-clojure-dummy-code-file-path)]
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
)