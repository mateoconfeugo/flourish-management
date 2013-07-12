;; DO NOT EDIT THIS FILE
;; This file was auto-generated by Configleaf.
;; Your changes could get overwritten by Configleaf.
;; DO NOT EDIT THIS FILE

(ns cfg.current)

(def project '{:compile-path "/Users/matthewburns/github/florish-online/src/clj/management/target/classes", :group "management", :global-vars {}, :checkout-deps-shares [:source-paths :test-paths :resource-paths :compile-path], :dependencies ([org.clojure/clojure "1.5.1"] [clj-aws-s3/clj-aws-s3 "0.3.6"] [compojure/compojure "1.1.5"] [clojurewerkz/scrypt "1.0.0"] [ring/ring-jetty-adapter "1.1.6"] [com.cemerick/friend "0.1.4"] [org.apache.httpcomponents/httpclient-osgi "4.2.5"] [me.raynes/fs "1.4.0"] [flourish-common/flourish-common "0.1.0"] [org.clojure/java.jdbc "0.2.3"] [mysql/mysql-connector-java "5.1.6"] [korma/korma "0.3.0-RC5"] [metis/metis "0.3.0"] [cheshire/cheshire "5.0.2"] [zookeeper-clj/zookeeper-clj "0.9.1"] [riemann-clojure-client/riemann-clojure-client "0.2.1"] [com.draines/postal "1.10.2"] [de.ubercode.clostache/clostache "1.3.1"] [expectations/expectations "1.4.33"] [org.clojure/tools.trace "0.7.5"] [vmfest/vmfest "0.3.0-alpha.5"] [org.clojars.tbatchelli/vboxjxpcom "4.2.4"] [clj-webdriver/clj-webdriver "0.6.0"] [ring-mock/ring-mock "0.1.3"] [ring/ring-devel "1.1.8"]), :plugin-repositories [["central" {:snapshots false, :url "http://repo1.maven.org/maven2/"}] ["clojars" {:url "https://clojars.org/repo/"}]], :app-configs {:foo "ham", :bar "sausage"}, :target-path "/Users/matthewburns/github/florish-online/src/clj/management/target", :name "management", :mailing-list {:archive "http://marketwithgusto.com/mgmt-gui-mailing-list-archives", :name "management gui dev mailing list", :other-archives ["http://marketwithgusto.com/mgmt-gui-list-archive2" "http://marketwithgusto.com/mgmt-gui-list-archive3"], :unsubscribe "list-unsubscribe@marketwithgusto.com", :subscribe "list-subscribe@marketwithgusto.com", :post "list@marketwithgusto.com"}, :deploy-repositories [["clojars" {:username :gpg, :url "https://clojars.org/repo/", :password :gpg}]], :root "/Users/matthewburns/github/florish-online/src/clj/management", :offline? false, :source-paths ("/Users/matthewburns/github/florish-online/src/clj/management/src"), :certificates ["clojars.pem"], :ring {:handler management.handler/app}, :hooks [configleaf.hooks], :version "0.1.0", :jar-exclusions [#"^\."], :profiles {:dev {:dependencies [[ring-mock "0.1.3"] [ring/ring-devel "1.1.8"]]}, :production {:ring {:stacktraces? false, :auto-reload? false, :open-browser? false}}, :pallet {:dependencies [[com.palletops/pallet "0.8.0-beta.9"] [com.palletops/pallet-vmfest "0.3.0-alpha.4"] [vmfest "0.3.0-alpha.5"] [org.clojars.tbatchelli/vboxjxpcom "4.2.4"] [com.palletops/java-crate "0.8.0-beta.4"] [com.palletops/runit-crate "0.8.0-alpha.1"] [com.palletops/app-deploy-crate "0.8.0-alpha.1"] [org.cloudhoist/pallet-jclouds "1.5.2"] [org.jclouds/jclouds-all "1.5.5"] [org.jclouds.driver/jclouds-slf4j "1.4.2" :exclusions [org.slf4j/slf4j-api]] [org.jclouds.driver/jclouds-sshj "1.4.2"] [ch.qos.logback/logback-classic "1.0.9"] [org.slf4j/jcl-over-slf4j "1.7.3"]]}}, :prep-tasks ["javac" "compile"], :url "http://marketwithgusto.com/mgnt/about/development.html", :repositories (["central" {:snapshots false, :url "http://repo1.maven.org/maven2/"}] ["clojars" {:url "https://clojars.org/repo/"}] ["releases" {:passphrase :env, :username :env, :url "s3p://marketwithgusto.repo/releases"}]), :resource-paths ("/Users/matthewburns/github/florish-online/src/clj/management/dev-resources" "/Users/matthewburns/github/florish-online/src/clj/management/resources"), :uberjar-exclusions [#"(?i)^META-INF/[^/]*\.(SF|RSA|DSA)$"], :main management.handler, :jvm-opts ["-XX:+TieredCompilation" "-XX:TieredStopAtLevel=1"], :eval-in :subprocess, :plugins ([lein-ring/lein-ring "0.8.2"] [lein-pprint/lein-pprint "1.1.1"] [s3-wagon-private/s3-wagon-private "1.1.2"] [com.palletops/pallet-lein "0.6.0-beta.9"] [lein-expectations/lein-expectations "0.0.7"] [lein-autoexpect/lein-autoexpect "0.2.5"] [configleaf/configleaf "0.4.6"] [lein-cloverage/lein-cloverage "1.0.2"]), :uberjar-name ["management-standalone"], :native-path "/Users/matthewburns/github/florish-online/src/clj/management/target/native", :description "management gui for the overall system to administer users, accounts and relevant operations", :test-paths ("/Users/matthewburns/github/florish-online/src/clj/management/test")})

(alter-var-root #'project with-meta '{:included-profiles (:default), :without-profiles {:compile-path "target/classes", :group "management", :global-vars {}, :dependencies ([org.clojure/clojure "1.5.1"] [clj-aws-s3/clj-aws-s3 "0.3.6"] [compojure/compojure "1.1.5"] [clojurewerkz/scrypt "1.0.0"] [ring/ring-jetty-adapter "1.1.6"] [com.cemerick/friend "0.1.4"] [org.apache.httpcomponents/httpclient-osgi "4.2.5"] [me.raynes/fs "1.4.0"] [flourish-common/flourish-common "0.1.0"] [org.clojure/java.jdbc "0.2.3"] [mysql/mysql-connector-java "5.1.6"] [korma/korma "0.3.0-RC5"] [metis/metis "0.3.0"] [cheshire/cheshire "5.0.2"] [zookeeper-clj/zookeeper-clj "0.9.1"] [riemann-clojure-client/riemann-clojure-client "0.2.1"] [com.draines/postal "1.10.2"] [de.ubercode.clostache/clostache "1.3.1"] [expectations/expectations "1.4.33"] [org.clojure/tools.trace "0.7.5"] [vmfest/vmfest "0.3.0-alpha.5"] [org.clojars.tbatchelli/vboxjxpcom "4.2.4"] [clj-webdriver/clj-webdriver "0.6.0"]), :plugin-repositories [["central" {:snapshots false, :url "http://repo1.maven.org/maven2/"}] ["clojars" {:url "https://clojars.org/repo/"}]], :app-configs {:foo "ham", :bar "sausage"}, :target-path "target", :name "management", :mailing-list {:archive "http://marketwithgusto.com/mgmt-gui-mailing-list-archives", :name "management gui dev mailing list", :other-archives ["http://marketwithgusto.com/mgmt-gui-list-archive2" "http://marketwithgusto.com/mgmt-gui-list-archive3"], :unsubscribe "list-unsubscribe@marketwithgusto.com", :subscribe "list-subscribe@marketwithgusto.com", :post "list@marketwithgusto.com"}, :deploy-repositories [["clojars" {:username :gpg, :url "https://clojars.org/repo/", :password :gpg}]], :root "/Users/matthewburns/github/florish-online/src/clj/management", :offline? false, :source-paths ("src"), :certificates ["clojars.pem"], :ring {:handler management.handler/app}, :hooks [configleaf.hooks], :version "0.1.0", :jar-exclusions [#"^\."], :profiles {:dev {:dependencies [[ring-mock "0.1.3"] [ring/ring-devel "1.1.8"]]}, :production {:ring {:stacktraces? false, :auto-reload? false, :open-browser? false}}, :pallet {:dependencies [[com.palletops/pallet "0.8.0-beta.9"] [com.palletops/pallet-vmfest "0.3.0-alpha.4"] [vmfest "0.3.0-alpha.5"] [org.clojars.tbatchelli/vboxjxpcom "4.2.4"] [com.palletops/java-crate "0.8.0-beta.4"] [com.palletops/runit-crate "0.8.0-alpha.1"] [com.palletops/app-deploy-crate "0.8.0-alpha.1"] [org.cloudhoist/pallet-jclouds "1.5.2"] [org.jclouds/jclouds-all "1.5.5"] [org.jclouds.driver/jclouds-slf4j "1.4.2" :exclusions [org.slf4j/slf4j-api]] [org.jclouds.driver/jclouds-sshj "1.4.2"] [ch.qos.logback/logback-classic "1.0.9"] [org.slf4j/jcl-over-slf4j "1.7.3"]]}}, :prep-tasks ["javac" "compile"], :url "http://marketwithgusto.com/mgnt/about/development.html", :repositories (["central" {:snapshots false, :url "http://repo1.maven.org/maven2/"}] ["clojars" {:url "https://clojars.org/repo/"}] ["releases" {:passphrase :env, :username :env, :url "s3p://marketwithgusto.repo/releases"}]), :resource-paths ("resources"), :uberjar-exclusions [#"(?i)^META-INF/[^/]*\.(SF|RSA|DSA)$"], :main management.handler, :jvm-opts nil, :eval-in :subprocess, :plugins ([lein-ring/lein-ring "0.8.2"] [lein-pprint/lein-pprint "1.1.1"] [s3-wagon-private/s3-wagon-private "1.1.2"] [com.palletops/pallet-lein "0.6.0-beta.9"] [lein-expectations/lein-expectations "0.0.7"] [lein-autoexpect/lein-autoexpect "0.2.5"] [configleaf/configleaf "0.4.6"] [lein-cloverage/lein-cloverage "1.0.2"]), :uberjar-name ["management-standalone"], :native-path "target/native", :description "management gui for the overall system to administer users, accounts and relevant operations", :test-paths ("test")}})