(ns cms.handler
  (:use [cms.controllers.document-manager]
        [cms.controllers.site-builder]
        [compojure.core :only [routes]]
        [compojure.route :as route]        
        [ring.adapter.jetty :as ring]        
        [ring.middleware.keyword-params :only [wrap-keyword-params]]        
        [ring.middleware.params :only [wrap-params]]        
        [ring.middleware.logger :only [wrap-with-logger]]
        [ring.middleware.format]))

(def cms-routes (routes document-routes
                        editor-routes
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

(def app  (wrap-params (wrap-keyword-params  (wrap-spy (wrap-with-logger cms-routes)))))

(defn start-cms-mgmt [port]
  (run-jetty app {:port port :join? false}))

(defn -main []
  (let [port (Integer/parseInt (or (System/getenv "PORT") "8088"))]
    (start-cms-mgmt port)))

