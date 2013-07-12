(ns cms.document
  "Business logic streams for content processing"
  (:use [flourish-common.pipeline :only[defpipe defpipeline return]]))

(defn get-document-resources [doc-id])
(defn get-content-document [doc-id])
(defn get-meta-document [doc-id])

(defpipe setup [doc-id channel-id]
  (return 
   (:set semantic-content (get-content-document doc-id))
   (:set meta-content (get-meta-document doc-id))))

(defpipe push-document-to-editor [alpha beta]
  (return (:set snippets (+ alpha beta))))

(defpipe preview-document [alpha beta]
  (return (:set snippets (+ alpha beta))))

(defpipe build-document-editor [alpha beta]
  (return (:set snippets (+ alpha beta))))

(defpipe assemble-snippets [alpha beta]
  (return (:set snippets (+ alpha beta))))

(defpipe transform [alpha beta]
  (return (:set snippets (+ alpha beta))))

(defpipe render [delta]
  (return
   (assoc-in [:x :y] 42)
   (:update delta * 2)
   (:set gamma (+ delta 100))))

(defpipe package-document [doc-id published-model]
  (return (:set resources (get-document-resources doc-id))))

(defpipe distribute [alpha beta gamma delta]
  (println " Alpha is" alpha "\n"
           "Beta is" beta "\n"
           "Delta is" delta "\n"
           "Gamma is" gamma)
  (return))


(defpipeline preview-document
  assemble-snippets
  render)

(defpipeline delete-document)


(defpipeline publish-document
  setup
  transform
  distribute)

(defpipeline edit-document
  setup
  build-document-editor
  push-document-to-editor)  

(defpipeline save-document
  setup
  build-document-editor
  push-document-to-editor)
