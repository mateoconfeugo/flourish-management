(ns test.cms.authoring-utils
  (:import [java.io BufferedReader InputStreamReader])
  (:use [clojure.test :only (is testing deftest)]
        [clojure.java.io]
        [cms.authoring-utils]
        [expectations]
        [fs.core :as fs :exclude [copy file]]        
        [net.cgrand.enlive-html :as html]))

;;        [com.ashafa.clutch.http-client]
;;        [com.ashafa.clutch :only [put-attachment get-attachment get-document
;;                                             put-document create-database delete-database]]        

;; Values used in tests
(def test-cfgs {:db-name "test-cms"
                :dom "<div id='test-dom'><h1>bar</h1></div>"
                :layout "<html><body><div class='uuid-1' id='host'>foo</div></body></html>"
                :xpath "/html/body/div/"
                :ls-id 1
                :base-dir "/tmp/cms/"
                :uuid 1})
;;                :uuid (new-uuid)})

(def test-model {:xpath (:xpath test-cfgs)
                 :dom (:dom test-cfgs)
                 :layout (:layout test-cfgs)
                 :uuid (:uuid test-cfgs)
                 :base-dir (:base-dir test-cfgs)})

(def model {:message "bingo"})
(def test-uuid (:uuid test-model))
(def test-snip-name (str "snip-" test-uuid))
(def tmpl-path  (str (System/getProperty "user.dir") "/test/cms/" test-uuid ".html"))
(def resource-tmpl-path  "templates/1.html")
(def test-hosting-element "div")
(def selector [(keyword (str test-hosting-element ".uuid-" test-uuid))])
(spit tmpl-path (:layout test-model) :append true)
(def test-rule (list '[:div.uuid-1] '(html/content model)))

;;# TEST create a snippet code
(def test-form (update-snippet-form {:uuid test-uuid :tmpl-path resource-tmpl-path}))
(eval test-form)
(expect true (= "bacon" (first (:content (first (snippet-1 "bacon"))))))

;; #TEST saving the snippet code
(def snip-path (str "/" "tmp/" "test_" test-uuid ".clj"))
(def clj-snip-file (file snip-path))
(save-s-expression clj-snip-file test-form)
(expect true (fs/exists? snip-path))

;; #TEST loading the form for file and running it
(def retreived-form (load-s-expression clj-snip-file))
(expect (= retreived-form test-form))
(eval retreived-form)
(expect true (= "bacon" (first (:content (first (snippet-1 "bacon"))))))

;; # TEST saving and loading a snippet form to a couch document as an attachment
(clutch/delete-database db)
(def db (clutch/create! (clutch/couch (:db-name test-cfgs))))
(def test-doc (clutch/put-document db {:javascript [] :css [] :snippets []}))
(save-snippet-form {:db db :doc test-doc :form test-form :uuid test-uuid})
(def retreived-form (load-snippet-form  {:db db :doc test-doc :uuid test-uuid}))
(eval retreived-form)
(def retreived-results (snippet-1 "bacon"))
(eval test-form)
(def test-results (snippet-1 "bacon"))
(expect true (= retreived-results test-results))
;; #TEST make sure test database deleted
(expect true (= true (:ok (clutch/delete-database db))))

;;# Test saving all the resource related to a snippet
(clutch/delete-database db)
(def db (clutch/create! (clutch/couch (:db-name test-cfgs))))
(def test-doc (clutch/put-document db {:javascript [] :css [] :snippets []}))
(save-snippet {:db db
               :doc test-doc
               :selector selector
               :form test-form
               :uuid test-uuid
               :rule test-rule
               :html (:layout test-cfgs)
               :css ".div"})

(clutch/update-document db test-doc {:css  [".div { color: red;}"]}) 

(comment
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