(ns management.handler
  (:use [cemerick.friend :only[authorize logout authenticate]]
        [cemerick.friend.workflows :only[interactive-form]]
        [cms.controllers.document-manager]
        [cms.controllers.site-builder]        
        [compojure.core :only [defroutes GET routes ANY]]
        [net.cgrand.enlive-html :only[emit*]]
        [compojure.route]
        [compojure.handler :as handler]        
        [compojure.route :only[files resources not-found]]
        [management.config :only[users]]
        [management.controllers.public :only[public-routes]]
        [management.controllers.tools :only[user-mgmt-routes]]
        [management.controllers.admin :only[admin-mgmt-routes]]
        [ring.adapter.jetty :as ring]
        [ring.util.response :only[file-response redirect content-type]]
        [ring.middleware.params :only [wrap-params]])
  (:gen-class))        

;;(def app (handler/site (wrap-params (routes public-routes user-mgmt-routes authenticate admin-mgmt-routes))))
;;(def app (handler/site (wrap-params (routes  user-mgmt-routes))))
(defroutes config-route (GET "/clientconfig" [] (content-type (file-response "clientconfig.json" {:root "resources"})  "application/json")))

(def app (handler/site (wrap-params (routes
                                     user-mgmt-routes
                                     document-routes
                                     editor-routes                                     
                                     public-routes
                                     config-route
                                     (resources "/")
                                     (files "/" {:root "public"})
                                     (not-found "Not Found")))))

;;(def app (handler/site  (routes public-routes user-mgmt-routes authenticate admin-mgmt-routes)))

(defn start [port] (run-jetty app {:port port :join? false}))

(defn -main []
  (let [port (Integer/parseInt (or (System/getenv "MGNT_PORT") "8087"))]
    (start port)))

(comment
(derive ::admin ::user)
(derive ::publisher ::user)
(derive ::advertiser ::user)
(derive ::content-provider ::user)
(derive ::feed-provider ::user)

  
(def app (handler/site (wrap-params (routes public-routes
                                            (authenticate user-mgmt-routes {:credential-fn (partial bcrypt-credential-fn users)
                                                                            :workflows [(interactive-form)]})
                                            (authenticate admin-mgmt-routes {:credential-fn (partial bcrypt-credential-fn admin)
                                                                             :workflows [(interactive-form)]})
                                            (logout (ANY "/logout" request (redirect "/")))))))
)
