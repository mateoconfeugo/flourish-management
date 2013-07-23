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

;; NETWORK ADMIN
(deftest operations-can-create-network false)
(deftest operations-can-remove-network false)
(deftest operations-can-enable-network false)
(deftest operations-can-disable-network false)

(expect true operations-can-create-network)
(expect true operations-can-remove-network)
(expect true operations-can-enable-network)
(expect trueoperations-can-disable-network)
