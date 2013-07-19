(ns cms.core-test
  (:use [clojure.test :only (is testing deftest)]
        [ring.mock.request]
        [landing-site.config]
        [cms.site]
        [me.raynes.fs :only(directory?) :as fs]))

(def home-dir (System/getProperty "user.home"))
(def test-root-dir (str home-dir "/github/florish-online/src/clj/landing-site/test"))
(def test-cfg-path (str test-root-dir "/test_system_config.json"))    
(def test-cfg (read-config (new-config {:cfg-file-path test-cfg-path})))
(:landing-site test-cfg)
(def test-site-dir (-> test-cfg :landing-site :cms-root-dir))
(def test-base-path "/Users/matthewburns/github/florish-online/src/clj/landing-site/test/test_site")
(def test-landing-site-id 1)
(def test-market-vector 1)

(deftest get-landing-site-test
    (let [id (get-landing-site-id 1 test-base-path)]
      (is (= 1 id) "got landing-site id")))


(deftest test-assemble-site-files
  (testing "extracting the path data from the data encoded in a request"
    (let [files ["1.html" "11.html" "12.html" "13.html" "14.html" "15.html" "16.html" "17.html"]]
      (is (= (files (rest (assemble-site-files test-landing-site-id test-base-path))))))))

(deftest test-populate-contents
  (testing "extracting the path data from the data encoded in a request"
    (let [test-files (assemble-site-files test-landing-site-id test-base-path)
          expected "Support Groups"
          pages (populate-contents test-files test-market-vector test-base-path)
          page (first pages)]
      (is (= expected (:header page)) "populating the contents of the pages"))))

(deftest test-get-site-contents
  (testing "extracting the path data from the data encoded in a request"
    (let [cms (new-cms-site {:base-path (-> test-cfg :landing-site :cms-root-dir)})
          contents (get-site-contents cms test-market-vector)
          expected "Support Groups"
          page (first contents)]
      (is (= expected (:header page)) "populating the contents of the pages"))))

(deftest test-get-site-menu
  (testing "extracting the path data from the data encoded in a request"
    (let [cms (new-cms-site {:webdir  website-dir :domain-name "patientcomfortreferral.com" :market-vector-id 1})
          menu (get-site-menue cms)
          expected "Support Groups"
          page (first contents)]
      (is (= expected (:header page)) "populating the contents of the pages"))))


(def x-client (new-cms-site {:webdir  website-dir :domain-name "patientcomfortreferral.com" :market-vector-id 1}))

(show-settings x-client)
(get-site-contents x-client)



(def path (-> test-cfg :landing-site :cms-root-dir))
(def cms (new-cms-site {:webdir  website-dir :domain-name "patientcomfortreferral.com" :market-vector-id 1}))
(def menu (get-site-menu cms))
(def fonts (get-fonts cms))

