;; ACCEPTANCE TESTS
(ns test.cms.acceptance
  (:use [clj-webdriver.taxi :only [set-driver! to click exists? find-element
                                   input-text submit quit drag-and-drop-by] :as scraper]
        [clojure.core]
        [cms.handler]
        [expectations]
        [pallet.action :only[with-action-options]]
        [pallet.configure :only[compute-service]]
        [pallet.api :only[converge]]))

;; USER ADMIN
(deftest administrator-can-create-operator false)
(deftest operator-can-create-network-operator false)
(deftest network-operator-can-create-landing-site-operator false)
(deftest landing-site-operator-can-create-content-editor false)
(deftest landing-site-operator-can-create-content-contributor false)
(deftest landing-site-operator-can-create-web-designer false)
(deftest landing-site-operator-can-create-site-admin false)
(deftest administrator-can-assign-features-to-roles false)
(deftest administrator-can-add-features false)
(deftest user-change-change-password false)
