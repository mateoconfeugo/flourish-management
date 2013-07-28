(ns cms.document
  "Business logic streams for content processing"
  (:require [com.ashafa.clutch :as clutch])
  (:use [flourish-common.pipeline :only[defpipe defpipeline return]]
        [clojure.core]
        [cms.authoring-utils :only [get-attached-code-file load-s-expression]]        
        [cms.resources.item :only [create-item get-item]]
        [cms.resources.common :as common]
        [cms.resources.collection :as collection :only [create-collection]]))

(defn beget [o p] (assoc o ::prototype p))

(def put assoc)

(defn get [m k]
  (when m
    (if-let [[_ v] (find m k)] v (recur (::prototype m) k))))

(def clone (partial beget {}))

(defn children
  [hierarchy parent]
  "Gets only the children of the parent"
  (filter #(= #{parent} (parents hierarchy %)) (descendants hierarchy parent)))

(defn child?
  [hierarchy child parent]
  "Predicate testing if the entity is an immediate child of the parent"
  (let [kids (children hierarchy parent)]
    (if (some #{child} kids) true false)))

(defn generate-composition-heirarchy
  [entity-types]
  (let [heirarchy (make-hierarchy)]
    (doseq [et entity-types]
      (doseq [component (:components et)]
        (derive heirarchy component et)))))
        
(defn define-doc-type
  [{:keys [entity-types doc-type-name db] :as options}]
  "Create a new document type along with composition contraints from
   a vector of entities"
  (let [entity-hierarchy (generate-composition-heirarchy entity-types)
        _ (create-collection doc-type-name)
        biz-model-schema (create-item "business-models" {:document-definition entity-types})]
        entity-hierarchy))

(defn get-doc-type [db name]
  (get-item db "business-models" name))

(defn walk-n-gather [links] )

(defn get-biz-model
  [coll slug-id]
  "Assemble the data according to the composition hierarchy by
   iterating over the items in the entity links collection and
   retreiving the child data from the uri and insert it into the
   parent collection"
  (let [model-item (get-item coll slug-id)
        model (walk-n-gather (:entity-links model-item))]
    (assoc model :type coll :key slug-id)))

(defn get-entity-hierarchy
    [{:keys [db doc-or-id] :as options}]
    (let [doc (if (map? doc-or-id) (clutch/get-document db (:_id doc-or-id)) (clutch/get-document db doc-or-id))
          coll (:collection doc)
;;          schema (get-schema coll)
          src-code-file (get-attached-code-file db doc "entity_hierarchy.clj")
          s-expression (load-s-expression src-code-file)]
      (eval s-expression)))

(defn create-entity-link [child parent] {})

(defn link-to
  [model child parent]
  "Takes a parent and child entity and link them together in the context of the model
   providing the constraints inherent in the composition heirarchy are met"
  (let [entities (:entities model)
        heirarchy (get-entity-hierarchy (:type model))
        is-child-of? (partial child? heirarchy (:type child))]
    (if (is-child-of? (:type parent))
      (update-in model :entities (conj entities (create-entity-link child parent)))
      nil)))

;; TODO: Handle multiple parents
(defn add-component 
  [hierarchy entity component]
  (if (child? hierarchy (:type component) (:type entity))
    (beget component entity)
    nil))

(defn get-document-resources [doc-id])
(defn get-content-document [doc-id])
(defn get-meta-document [doc-id])

(comment
(defn process-elements
  [elements]
    "Recursively builds up dom rendered cms heirarctical document"
    (doseq [element elements]
      (if (contains? element :elements)
        (process-elements (:elements element))
        (let [binding (get-dom-binding (:id element))
              uuid (:uuid binding)]
          (uuid (:fields element))))))
)
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
