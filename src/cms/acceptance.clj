;; ACCEPTANCE TESTS
(ns cms.acceptance
  (:use [clj-webdriver.taxi :only [set-driver! to click exists? find-element
                                   input-text submit quit drag-and-drop-by] :as scraper]
        [clojure.core]
        [cms.handler]
        [expectations]
        [pallet.action :only[with-action-options]]
        [pallet.configure :only[compute-service]]
        [pallet.api :only[converge]]))

(defn  get-test-port []
  8088)

(defn  get-server-address [&mode address]
  "http://localhost")

(deftest user-can-access-landing-site-html-editor
  (let [app-server (start-lsbs test-port)
        target-uri (str (get-server-address) ":" (get-test-port) "/cms/sitebuilder"))
        _ (scraper/set-driver! {:browser :firefox})
        _ (scraper/to target-uri)
        current-uri (scraper/current-url)
        _ (.stop app-server)]
        _ (scraper/quit)    
    (is  (= current-uri target-uri)))

(deftest user-can-edit-landing-site-html-structure
  (let [app-server (start-lsbs test-port)
        _ (scraper/set-driver! {:browser :firefox})
        _ (scraper/to (str (get-server-address) ":" get-test-port "/cms/sitebuilder/ls-id/1/uuid/1"))
        _ (scraper/input-text "#lead_full_name" "test matt")
        _ (scraper/drag-and-drop-by "#test-control" {:x 20 :y -5})
        ele (scraper/find-element test-xpath)
        matching (count (scraper/find-element "//*[starts-with(@class,'uuid_')]"))
        _ (scraper/quit)        
        _ (.stop app-server)]
    (is  (= 1 matching))))

(deftest user-can-create-landing-site false)
(deftest user-can-save-landing-site false)
(deftest use-can-publish-landing-site-to-delivery false)
(deftest use-can-delete-landing-site false)
(deftest user-can-edit-element-css false)
(deftest user-can-add-stylesheet false)
(deftest user-can-set-stylesheet-active false)
(deftest user-can-create-document false)
(deftest user-can-delete-document false)
(deftest user-can-edit-document false)
(deftest user-can-publish-document false)
(deftest user-can-preview-document false)
(deftest user-can-search-documents false)
(deftest user-can-list-documents false)
(deftest user-can-add-document-to-landing-site false)
(deftest user-can-remove-document-to-landing-site false)
(deftest user-can-bind-document-element-to-html-control false)
(deftest user-can-unbind-document-element-to-html-control false)
(deftest user-can-edit-bound-cms-content-in-html-editor false)
(deftest user-can-link-landing-site-to-market-vector false)
(deftest user-can-clone-landing-site false)

;; HTML EDITING
(expect true user-can-access-landing-site-html-editor)
(expect true user-can-create-landing-site)
(expect true user-can-edit-landing-site-html-structure)
(expect true user-can-save-landing-site)
(expect true user-can-delete-landing-site)
;; CSS EDITING
(expect true user-can-edit-element-css)
(expect true user-can-add-stylesheet)
(expect true user-can-set-stylesheet-active)
;; CMS EDITING
(expect true user-can-create-document)
(expect true user-can-delete-document)
(expect true user-can-edit-document)
(expect true user-can-publish-document)
(expect true user-can-preview-document)
(expect true user-can-search-documents)
(expect true user-can-list-documents)
;; Landing Site HTML Scaffolding CMS document interaction
(expect true user-can-add-document-to-landing-site)
(expect true user-can-remove-document-to-landing-site)
(expect true user-can-bind-document-element-to-html-control)
(expect true user-can-unbind-document-element-to-html-control)
(expect true user-can-edit-bound-cms-content-in-html-editor)
;; Landing Site Management
(expect true user-can-link-landing-site-to-market-vector)
(expect true user-can-clone-landing-site)
(expect true user-can-publish-landing-site-to-delivery)
