(ns cms.controllers.document-manager
  (:use [cms.document]
        [compojure.core :only [defroutes GET DELETE POST PUT]]))

(defroutes document-routes
  (GET "/document/:doc-id" [doc-id :as req] (preview-document {:document-id doc-id}))    
  (DELETE "/document/:doc-id" [doc-id :as req] (delete-document {:document-id doc-id}))
  (POST "/document/:doc-id" [doc-id :as req](save-document {:document-id doc-id :model (-> req :parms :model)}))
  (PUT "/document/:doc-id" [doc-id :as req] (save-document {:document-id doc-id :model (-> req :parms :model)}))
  (GET "/document/edit:doc-id" [doc-id :as req] (edit-document {:document-id doc-id}))
  (POST "/document/publish/:doc-id" [doc-id :as req] (publish-document {:document-id doc-id :model (-> req :params :publish-spec)})))
