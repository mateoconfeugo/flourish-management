(comment
(ns test.cms.controllers.site-builder
  (:use [cheshire.core :only (parse-string generate-string)]
        [clj-webdriver.taxi :only [set-driver! to click exists? input-text submit quit] :as scraper]
        [clojure.test :only (is testing deftest)]
        [cms.handler :only(app start-cms-mgmt)]        
        [expectations]
        [korma.core :only [defentity database insert values select where] :as kdb]   
        [net.cgrand.enlive-html]
        [ring.mock.request]))

;; COMPONENT TESTS
(def test-uuid "95e0404e-461d-4c81-b1bc-9d0ffce508af")
(def test-xpath "/html/body/div[2]/div/div[2]/div[2]")
(def ls-base-html-path "")
(def update-model-data {:uuid test-uuid
                        :xpath test-xpath
                        :dom (slurp "test_dom.json")
                        :layout (slurp "test_layout.json")})

(deftest update-framework-html-test
 (let [post-data (generate-string update-model-data)
       test-response (app (body (request :post  (str "/cms/sitebuilder/uuid/" test-uuid) post-data)))
       updated-html (slurp ls-base-html-path)
       test-snippet-select [(str "." uuid)]]
   (is (= (snippet-test update-html test-snippet-select) 1))))
  
(expect true (update-framework-html-test))

;; CLEANUP


  )