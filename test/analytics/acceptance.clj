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

;; ADVERTISER REPORTING ANALYTICS 
(deftest advertising-can-view-advertising-by-market-matrix-report false)
(deftest advertising-can-view-advertising-by-market-vector-report false)
(deftest advertising-can-view-advertising-by-landing-site-report false)
(deftest advertising-can-view-advertising-by-campaign-report false)
(deftest advertising-can-view-advertising-by-adgroup-report false)
(deftest advertising-can-view-advertising-by-ad-report false)
(deftest advertising-can-view-conversions-by-market-matrix-report false)
(deftest advertising-can-view-conversions-by-market-vector-report false)
(deftest advertising-can-view-conversions-by-landing-site-report false)
(deftest advertising-can-view-conversions-by-campaign-report false)
(deftest advertising-can-view-conversions-by-adgroup-report false)
(deftest advertising-can-view-conversions-by-ad-report false)
(deftest advertising-can-view-hits-by-market-matrix-report false)
(deftest advertising-can-view-hits-by-market-vector-report false)
(deftest advertising-can-view-hits-by-landing-site-report false)
(deftest advertising-can-view-hits-by-campaign-report false)
(deftest advertising-can-view-hits-by-adgroup-report false)
(deftest advertising-can-view-hits-by-ad-report false)

;; PUBLISHER REPORTING ANALYTICS 
(deftest publishing-can-view-impressions-per-site false)
(deftest publishing-can-view-impressions-per-source false)
(deftest publishing-can-view-impressions-per-subid false)
(deftest publishing-can-view-impressions-per-adnetwork false)
(deftest publishing-can-view-clicks-per-site false)
(deftest publishing-can-view-clicks-per-source false)
(deftest publishing-can-view-clicks-per-subid false)
(deftest publishing-can-view-revenue-per-site false)
(deftest publishing-can-view-revenue-per-source false)
(deftest publishing-can-view-revenue-per-subid false)
(deftest publishing-can-view-cpc-per-site false)
(deftest publishing-can-view-cpc-per-source false)
(deftest publishing-can-view-cpc-per-subid false)

;; BUSINESS OPERATIONS REPORTINGS
(deftest operations-can-view-cpc-per-adnetwork false)
(deftest operations-can-view-cpc-per-feed-channel false)
(deftest operations-can-view-cpc-per-ad-tier false)
(deftest operations-can-view-impressions-per-feed-channel false)
(deftest operations-can-view-impressions-per-ad-tier false)
(deftest operations-can-view-clicks-per-adnetwork false)
(deftest operations-can-view-clicks-per-feed-channel false)
(deftest operations-can-view-clicks-per-ad-tier false)
(deftest operations-can-view-revenue-per-adnetwork false)
(deftest operations-can-view-revenue-per-feed-channel false)
(deftest operations-can-view-revenue-per-ad-tier false)
