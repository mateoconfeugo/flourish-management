(ns management.controllers.public
  (:use [compojure.core :only [defroutes GET ANY]]
        [compojure.route :only [not-found files resources]]
        [ring.util.response :only [file-response redirect]]))
        
(defroutes public-routes
  (GET "/" [] (redirect "/login"))
  (GET "/login" [] (file-response "login.html" {:root "resources"})))





