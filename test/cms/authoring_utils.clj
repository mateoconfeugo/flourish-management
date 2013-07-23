(ns cms.authoring-utils
  (:refer-clojure :exclude [assoc! conj! dissoc! name parents]) ; suppress the shadowing warning  
  (:require [clojure.core :as core]
            [cms.authoring-utils]) 
  (:import [java.io BufferedReader InputStreamReader])
  (:use [clojure.test :only (is testing deftest)]
        [clojure.java.io]
        [expectations]
        [me.raynes.fs :as fs :exclude [copy file]]                        
        [net.cgrand.enlive-html :as html]
        [com.ashafa.clutch.http-client]
        [com.ashafa.clutch ]))

;; Values used in tests
(def test-cfgs {:db-name "test-cms"
                :dom "<div id='test-dom'><h1>bar</h1></div>"
                :layout "<html><body><div class='uuid-1' id='host'>foo</div></body></html>"
                :xpath "/html/body/div/"
                :ls-id 1
                :base-dir "/tmp/cms/"
                :uuid 1})

;; This will serve as the data from the post
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

;;# TEST First lets see if we can create some snippet code
(def test-form (cms.authoring-utils/update-snippet-form {:uuid test-uuid :tmpl-path resource-tmpl-path :selector selector :rule test-rule}))
(eval test-form)
(expect true (= "bacon" (first (:content (first (snippet-1 "bacon"))))))

;; #TEST Lets save and load the snippet code to and file from a file
(def tmp-file (fs/temp-file "snippet" ))
(def output-file (save-s-expression tmp-file test-form))
(def retreived-form (load-s-expression output-file))
(expect true (= retreived-form test-form))

;; #TEST Lets try running the clojure snippet code we created  stored in the file
(eval retreived-form)
(expect true (= "bacon" (first (:content (first (snippet-1 "bacon"))))))

;; # TEST Okay now lets save and load a snippet form to a couch document as an attachment
(if (in? (clutch/all-databases) (:db-name test-cfgs)) (clutch/delete-database "test-cms"))
(def db (clutch/create! (clutch/couch (:db-name test-cfgs))))
(def test-doc (clutch/put-document db {:javascript [] :css [] :snippets []}))
(save-snippet-form {:db db :doc-or-id test-doc :form test-form :uuid test-uuid})
(def test-doc (clutch/get-document db (:_id test-doc)))
(def code (fs/temp-file "snippet" ))
(spit code (get-attached-code-file db test-doc "snippet_1.clj"))
(load-s-expression code)
(def retreived-form (load-snippet-form  {:db db :doc-or-id  (:_id test-doc) :uuid test-uuid}))
(eval retreived-form)
(def retreived-results (snippet-1 "bacon"))
(eval test-form)
(def test-results (snippet-1 "bacon"))
(expect true (= retreived-results test-results))
;; #TEST make sure test database deleted
(expect true (= true (:ok (clutch/delete-database db))))

;;# Test saving and retreiving all the resource related to a snippet
(def db (clutch/create! (clutch/couch (:db-name test-cfgs))))
(def test-doc (clutch/put-document db {:javascript [] :css [] :snippets []}))
(def test-doc (clutch/get-document db (:_id test-doc)))
(save-snippet {:db db
               :doc-or-id test-doc
               :selector selector
               :form test-form
               :uuid test-uuid
               :rule test-rule
               :html (:layout test-cfgs)
               :css ".div"})
(expect true (= ((get db (:_id test-doc)) :html) (:layout test-cfgs)))
(def retreived-snippet (load-snippet {:db db :doc-or-id test-doc :uuid 1}))
(expect true (= (:html retreived-snippet) (:layout test-cfgs)))

;;# Test reading snippet rules to an enlive template
(def template-form (create-template-form {:template-path (str (System/getProperty "user.dir") "/resources/base_template.clj")
                                              :rules [ [:1 'snippet-1 {:foo 1} ] [:2 'snippet-3 {:bar 3}]  ]
                                              :output "/tmp/bone.clj"
                                          :namespace 'cms.authoring-utils}))
(defn snippet-1 [model] model)
(defn snippet-3 [model] model)

;;# Test injecting the snippets macro defintion into the enliven template macro in the clojure source code file
(def ham [retreived-form retreived-form])
(def src-code (create-view-template-file {:tmpl-form template-form :rules ham  :output-file "test/generated_enlive_template.clj"}))
(load-file "test/generated_enlive_template.clj")
(expect true (contains? (first (html/html-snippet (first (cms.authoring-utils/domain-ls-id {})))) :tag))
(expect true (boolean (resolve 'cms.authoring-utils/domain-ls-id)))
(expect true (boolean (resolve 'cms.authoring-utils/snippet-1)))
(expect true (boolean (resolve 'cms.authoring-utils/snippet-3)))
(expect false (boolean (resolve 'cms.authoring-utils/snippet-2)))
(fs/delete "test/generated_enlive_template.clj")


