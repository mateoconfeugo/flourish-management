(ns _
  (:refer-clojure :exclude [assoc! conj! dissoc! name parents])
  (:require [clojure.core :as core]) 
  (:use [flourish-common.web-page-utils :only [ render-to-response]]
        [net.cgrand.enlive-html]))

(deftemplate domain-ls-id "templates/base_template.html"
  [{:keys [model] :as settings}])



