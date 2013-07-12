(ns management.controllers.admin
  (:use [compojure.core :only [defroutes GET]]
        [management.views.admin :as admin :only [admin-dashboard]]
        [ring.util.response :only [content-type file-response]]))

(defroutes admin-mgmt-routes
  (GET "/clientconfig" [] (content-type (file-response "clientconfig.json" {:root "resources"})  "application/json"))
  (GET "/admin/:admin-id" [id] (admin-dashboard {})))

