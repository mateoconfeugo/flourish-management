(ns handler
  (:require [aws.sdk.s3 :as s3])
  (:use [expectations]
        [management.handler :only[app]]
        [management.views.snippets]
        [ring.mock.request]))

(def cred {:access-key "AKIAJ5RLKVIVMK2VBA6A", :secret-key "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"})
(s3/create-bucket cred "marketwithgusto.repo")
(s3/put-object cred "marketwithgusto.repo" "some-key" "some-value")
(println (slurp (:content (s3/get-object cred "marketwithgusto.repo" "some-key"))))


(def test-user-request {:request-method :get
                        :uri "/mgmt/user/1"
                        :headers {}
                        :params {:id 1}})

(def test-cfg-request {:request-method :get
                      :uri "/clientconfig"
                      :headers {}
                      :params {}})


(expect true (= (:status (app (request :get (:uri test-user-request)))) 200))
(expect true (= (:status (app (request :get "/clientconfig")) 200)))
(expect true (= (:status (app (request :get "/invalid-bogus-fake"))) 404))
(expect true (= (:status (app (request :get "/login"))) 200))
