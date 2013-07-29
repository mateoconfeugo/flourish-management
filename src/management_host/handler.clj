(ns management-host.handler
  (:require [management.handler])
  (:use  [ring.adapter.jetty :as ring :only[run-jetty]])
  (:gen-class))

(def app management.handler/app)

(defn start [port] (ring/run-jetty app {:port port :join? false}))

(defn -main []
  (let [port (Integer/parseInt (or (System/getenv "MGNT_PORT") "8087"))]
    (start port)))
