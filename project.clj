(defproject management "0.1.0"
  :description "management gui for the overall system to administer users, accounts and relevant operations"
  :url "http://marketwithgusto.com/mgnt/about/development.html"
  :license {:name "MIT License"
            :url "http://opensource.org/licenses/MIT"}  
  :resource-paths ["resources"]
  :uberjar-name ["management-standalone"]
  :min-lein-version "2.0.0"
  :dependencies [[cheshire "5.0.2"] ; JSON <-> clojure 
                 [clj-aws-s3 "0.3.6"] ; Access Amazons S3 bitbuckets                 
                 [clojurewerkz/scrypt "1.0.0"] ; Strong encryption and hashing
                 [com.ashafa/clutch "0.4.0-RC1"] ; CouchDB client https://github.com/clojure-clutch/clutch
                 [com.cemerick/friend "0.1.4"] ; Role based authentication                
                 [com.taoensso/timbre "2.2.0"] ; Logging https://github.com/ptaoussanis/timbre
                 [enlive "1.1.1"] ; DOM manipulating                  
                 [compojure "1.1.5"] ; Web routing https://github.com/weavejester/compojure
                 [de.ubercode.clostache/clostache "1.3.1"] ; Templationg
                 [flourish-common "0.1.0"] ; Common functionality of the gusto system                 
                 [ibdknox/clojurescript "0.0-1534"] ; ClojureScript compiler https://github.com/clojure/clojurescript
                 [korma "0.3.0-RC5"] ; ORM                                
                 [liberator "0.9.0"] ; WebMachine(REST state machine) port to clojure
                 [me.raynes/fs "1.4.1"] ; File manipulation tools                
                 [metis "0.3.0"] ; Form validation                
                 [mysql/mysql-connector-java "5.1.6"] ; mysql jdbc                 
                 [org.apache.httpcomponents/httpclient-osgi "4.2.5"]
                 [org.clojure/clojure "1.5.1"] ; Lisp on the JVM http://clojure.org/documentation                 
                 [org.clojure/java.jdbc "0.2.3"] ; jdbc client
                 [org.clojure/core.match "0.2.0-rc3"] ; Erlang-esque pattern matching https://github.com/clojure/core.match 
                 [riemann-clojure-client "0.2.1"] ; Monitoring client
                 [ring.middleware.logger "0.4.0"]                 
                 [ring/ring-jetty-adapter "1.2.0"] ; Web Server https://github.com/ring-clojure/ring
                 [zookeeper-clj "0.9.1"]] ; Zookeeper configuration client                
  :hooks [configleaf.hooks]
  :app-configs {:foo "ham" :bar "sausage"}
  :plugins [[lein-ring "0.8.2"]
            [lein-pprint "1.1.1"]
            [s3-wagon-private "1.1.2"]
            [com.palletops/pallet-lein "0.6.0-beta.9"]
            [lein-expectations "0.0.7"]
            [lein-autoexpect "0.2.5"]
            [configleaf "0.4.6"]
            [lein-cloverage "1.0.2"]]
  :ring {:handler management.handler/app
;         :init management.handler/init
;         :destroy management.handler/destroy}
         }
;  :repositories [["releases" {:url "s3p://marketwithgusto.repo/releases"
;                             :username "AKIAJ5RLKVIVMK2VBA6A"
;                             :passphrase "CEKqv6Ka/0BDc+UcbBpm3wycu25Wb7M0DywW7pE4"}]]
  :main management.handler
  :profiles  {:dev {:dependencies [[ring-mock "0.1.3"]
                                   [ring/ring-devel "1.1.8"]
                                   [clj-webdriver "0.6.0"]                 
                                   [expectations "1.4.33"]
                                   [org.clojure/tools.trace "0.7.5"]
                                   [vmfest "0.3.0-alpha.5"]]}                                                    
              :production {:ring
                           {:open-browser? false, :stacktraces? false, :auto-reload? false}}
              :pallet {:dependencies
                       [[com.palletops/pallet "0.8.0-beta.9"]
                        [com.palletops/pallet-vmfest "0.3.0-alpha.4"]
                        [vmfest "0.3.0-alpha.5"]
                        [org.clojars.tbatchelli/vboxjxpcom "4.2.4"]
                        [com.palletops/java-crate "0.8.0-beta.4"]
                        [com.palletops/runit-crate "0.8.0-alpha.1"]
                        [com.palletops/app-deploy-crate "0.8.0-alpha.1"]
                        [org.cloudhoist/pallet-jclouds "1.5.2"]
                        [org.jclouds/jclouds-all "1.5.5"]
                        [org.jclouds.driver/jclouds-slf4j "1.4.2"
                         :exclusions [org.slf4j/slf4j-api]]
                        [org.jclouds.driver/jclouds-sshj "1.4.2"]
                        [ch.qos.logback/logback-classic "1.0.9"]
                        [org.slf4j/jcl-over-slf4j "1.7.3"]]}}
  :mailing-list {:name "management gui dev mailing list"
                 :archive "http://marketwithgusto.com/mgmt-gui-mailing-list-archives"
                 :other-archives ["http://marketwithgusto.com/mgmt-gui-list-archive2"
                                  "http://marketwithgusto.com/mgmt-gui-list-archive3"]
                 :post "list@marketwithgusto.com"
                 :subscribe "list-subscribe@marketwithgusto.com"
                 :unsubscribe "list-unsubscribe@marketwithgusto.com"}
  

  )
