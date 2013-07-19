(ns test.cms.dummy-snippet
  (:use [net.cgrand.enlive-html :as html]
        [cheshire.core]
        [clojure.java.io])
  (:require [com.ashafa.clutch :as clutch]))

(html/defsnippet test-dummy buffer *dummy-select*
  [model]
  [:h1] (html/content "huh"))