(ns cms.handler
  (:use [cms.controllers.document-manager]
        [cms.controllers.site-builder :as sb :only [editor-routes]]
        [cms.api.collections :as collections]
        [cms.api.items :as items]
        [cms.db.views :as db-views]
        [compojure.core :only [routes]]
        [compojure.route :as route]
        [liberator.core :refer [resource defresource]]
        [liberator.dev :refer (wrap-trace)]        
        [ring.adapter.jetty :as ring]        
        [ring.middleware.keyword-params :only [wrap-keyword-params]]        
        [ring.middleware.params :only [wrap-params]]        
        [ring.middleware.logger :only [wrap-with-logger]]))
;;        [ring.middleware.format]

(def env 
  "Global Environment: ENV=debug/dev/prod (dev is default)"
  (or (System/getenv "ENV") "dev"))

(def cms-routes (routes document-routes
                        editor-routes
                        collections/collection-routes
                        items/item-routes
                        (route/resources "/")
                        (route/files "/" {:root "public/cms"})
                        (route/not-found "Not Found")))

(defn wrap-spy [handler]
  (fn [request]
    (println "-------------------------------")
    (println "Incoming Request:")
    (clojure.pprint/pprint request)
    (let [response (handler request)]
      (println "Outgoing Response Map:")
      (clojure.pprint/pprint response)
      (println "-------------------------------")
      response)))

;;(def app (-> cms-routes
;;             wrap-params
;;             wrap-keyword-params
;;             wrap-with-logger
;;             wrap-spy))

(def app  (wrap-params (wrap-keyword-params  (wrap-spy (wrap-with-logger (wrap-trace cms-routes :header :ui))))))

(defn start-cms-mgmt [port]
  (run-jetty app {:port port :join? false}))

(defn -main []
  (let [port (Integer/parseInt (or (System/getenv "PORT") "8088"))]
    (start-cms-mgmt port)))


(start-cms-mgmt 7777)

