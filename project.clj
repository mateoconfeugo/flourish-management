(defproject management-host "0.1.0"
  :description "The hosting release wrapper is faciliting the heroku method of releasing from a github repository
                and using a private jar repository to pull the application component jars in from. It ads no
                functionality of its own. Serves as place to run all the component acceptence tests"
  :url "http://marketwithgusto.com/mgnt/about/development.html"
  :license {:name "MIT License" :url "http://opensource.org/licenses/MIT"}
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.5.1"]  ; Lisp on the JVM
                 [management "0.1.0"]

                 [ring/ring-jetty-adapter "1.2.0"]]
  :plugins [[lein-ring "0.8.2"]
            [s3-wagon-private "1.1.2"]
            [lein-expectations "0.0.7"]
            [lein-autoexpect "0.2.5"]]
  :main management-host.handler
  :ring {:handler management-host.handler/app}
  :repositories [["private" {:url "s3p://marketwithgusto.repo/releases/"
                             :username :env
                             :passphrase :env}]]
  :profiles  {:dev {:dependencies [[ring-mock "0.1.3"]
                                   [ring/ring-devel "1.1.8"]
                                   [clj-webdriver "0.6.0"]
                                   [expectations "1.4.33"]]}}
  :mailing-list {:name "management gui dev mailing list"
                 :archive "http://marketwithgusto.com/mgmt-gui-mailing-list-archives"
                 :other-archives ["http://marketwithgusto.com/mgmt-gui-list-archive2"
                                  "http://marketwithgusto.com/mgmt-gui-list-archive3"]
                 :post "list@marketwithgusto.com"
                 :subscribe "list-subscribe@marketwithgusto.com"
                 :unsubscribe "list-unsubscribe@marketwithgusto.com"})
