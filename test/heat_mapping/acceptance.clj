;; ACCEPTANCE TESTS
(ns test.head-mapping.acceptance
  (:use [clj-webdriver.taxi :only [set-driver! to click exists? find-element
                                   input-text submit quit drag-and-drop-by] :as scraper]
        [clojure.core]
        [cms.handler]
        [expectations]
        [pallet.action :only[with-action-options]]
        [pallet.configure :only[compute-service]]
        [pallet.api :only[converge]]))

;; HEAT MAPPING
(deftest user-can-view-heatmap-summary-table-with-links-to-map false)
(deftest user-can-view-heat-map-for-landing-site-over-select-time-range false)
