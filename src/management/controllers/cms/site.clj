(ns management.controllers.cms.site
  (:use [compojure.core :only (defroutes GET POST)])
  (:require [clojure.string :as str]
            [ring.util.response :as ring]
            [management.views.admin :as view]
            [management.models.cms-config-file :as cms-site]))

(defn index []
  (view/index (cms-site/all)))

(defn create [site-settings]
  (when-not (str/blank? site-settings)
    (cms-site/create site-setting))
  (ring/redirect "/"))

(defroutes routes
  (GET  "/" [] (index))
  (POST "/" [site-settings] (create site-settings)))