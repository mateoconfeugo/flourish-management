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

;; FEED MANAGEMENT
(deftest administration-can-add-feed-provider false)
(deftest administration-can-remove-feed-provider false)
(deftest administration-can-enable-feed-provider false)
(deftest administration-can-disable-feed-provider false)
(deftest administration-can-add-feed-channel false)
(deftest administration-can-remove-feed-channel false)
(deftest administration-can-enable-feed-channel false)
(deftest administration-can-disable-feed-channel false)
(deftest administration-can-add-ad-tier false)
(deftest administration-can-remove-ad-tier false)
(deftest administration-can-enable-ad-tier false)
(deftest administration-can-disable-ad-tier false)
(deftest administration-can-set-feed-channel-rev-shave false)
(deftest administration-can-edit-feed-channel-rev-shave false)
(deftest operations-can-set-publisher-rev-shave false)
(deftest operations-can-edit-publisher-rev-shave false)
(deftest operations-can-add-feed-channel-to-ad-tier false)
(deftest operations-can-remove-feed-channel-to-ad-tier false)
(deftest operations-can-enable-feed-channel-to-ad-tier false)
(deftest operations-can-disable-feed-channel-to-ad-tier false)
(deftest operations-can-add-advertiser-to-feed-channel false)
(deftest operations-can-remove-advertiser-from-feed-channel false)
(deftest operations-can-enable-advertiser-for-feed-channel false)
(deftest operations-can-disable-advertiser-for-feed-channel false)
(deftest operations-can-add-advertiser-to-feed-provider false)
(deftest operations-can-remove-advertiser-from-feed-provider false)
(deftest operations-can-enable-advertiser-for-feed-provider false)
(deftest operations-can-disable-advertiser-for-feed-provider false)
(deftest operations-can-add-advertiser-to-feed-ad-tier false)
(deftest operations-can-remove-advertiser-from-ad-tier false)
(deftest operations-can-enable-advertiser-for-ad-tier false)
(deftest operations-can-disable-advertiser-for-ad-tier false)
(deftest operations-can-add-publisher-tier false)
(deftest operations-can-remove-publisher-tier false)
(deftest operations-can-enable-publisher-tier false)
(deftest operations-can-disable-publisher-tier false)
(deftest operations-can-add-publisher-source-to-tier false)
(deftest operations-can-remove-publisher-source-to-tier false)
(deftest operations-can-enable-publisher-source-to-tier false)
(deftest operations-can-disable-publisher-source-to-tier false)
(deftest operations-can-add-publisher-source-subid-to-tier false)
(deftest operations-can-remove-publisher-source-subid-to-tier false)
(deftest operations-can-enable-publisher-source-subid-to-tier false)
(deftest operations-can-disable-publisher-source-subid-to-tier false)
(deftest operations-can-remove-ad-tier false)
(deftest operations-can-enable-ad-tier false)
(deftest operations-can-disable-ad-tier false)
