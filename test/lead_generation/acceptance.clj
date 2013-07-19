;; ACCEPTANCE TESTS
(ns test.lead-generation.acceptance
  (:use [clj-webdriver.taxi :only [set-driver! to click exists? find-element
                                   input-text submit quit drag-and-drop-by] :as scraper]
        [clojure.core]
        [cms.handler]
        [expectations]
        [pallet.action :only[with-action-options]]
        [pallet.configure :only[compute-service]]
        [pallet.api :only[converge]]))

;; LEAD GENERATION
(deftest user-can-view-lead-summary-table-with-detail-links false)
(deftest user-can-view-lead-detail false)
(deftest user-can-download-csv false)
(deftest user-can-download-excel false)
