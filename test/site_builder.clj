(ns test.site-builder
  (:use [cheshire.core :only (parse-string generate-string)]
        [management.handler :only [app]]
        [clojure.test :only (is testing deftest)]
        [expectations]
        [net.cgrand.enlive-html :exclude [select]]
        [korma.core :only [defentity database insert values select where] :as kdb]           
        [ring.mock.request]))

;;
;;        [clj-webdriver.taxi :only [set-driver! to click exists? input-text submit quit] :as scraper]        
;;        [cms.handler :only(app start-cms-mgmt)]        


;; COMPONENT TESTS
(def test-uuid "95e0404e-461d-4c81-b1bc-9d0ffce508af")
(def test-xpath "/html/body/div[2]/div/div[2]/div[2]")
(def ls-base-html-path "")
(def base-dir (str (System/getProperty "user.dir") "/test/data/"))
(def update-model-data {:uuid test-uuid
                        :xpath test-xpath
                        :dom (slurp (str base-dir "test_dom.json"))
                        :layout (slurp (str base-dir "test_layout.json"))})

(def test-post-data (generate-string update-model-data))
(def test-response (app (body (request :post  (str "/cms/sitebuilder/uuid/" test-uuid) test-post-data) 200))
(def updated-html (slurp ls-base-html-path))
(def test-snippet-select [(str "." uuid)])
   (is (= (snippet-test update-html test-snippet-select) 1))))
  
(expect true (update-framework-html-test))

;; CLEANUP
