(ns cms.controllers.site-builder
  (:use [cheshire.core :only [parse-string generate-string]]
        [cms.views.builder :only [render]]
        [compojure.core :only [defroutes GET DELETE POST PUT]]))
        

(defroutes editor-routes   (GET "/cms/sitebuilder"  req (render req)))

;;  (PUT "/cms/sitebuilder/:ls-id" [ls-id :as req]
;;    (content-type (response (save-landing-site-test (-> req :params))) "application/json"))
;;  (POST "/cms/sitebuilder/ls-id/:ls-id/uuid/:uuid" [ls-id uuid :as req]
;;    (content-type (response (update-landing-site (assoc (-> req :params) :ls-id ls-id))) "application/json"))
