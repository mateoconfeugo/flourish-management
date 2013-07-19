;; ACCEPTANCE TESTS
(ns test.account.acceptance
  (:use [clj-webdriver.taxi :only [set-driver! to click exists? find-element
                                   input-text submit quit drag-and-drop-by] :as scraper]
        [clojure.core]
        [cms.handler]
        [expectations]
        [pallet.action :only[with-action-options]]
        [pallet.configure :only[compute-service]]
        [pallet.api :only[converge]]))

;; ACCOUNT MANAGEMENT
(deftest person-can-sign-up-for-user-account false)
(deftest user-account-assigned-roles false)
(deftest new-user-landing-site-wizard-experience false)
