(ns cms.resources.collection
  (:use [clojure.walk]
        [cms.resources.common :as common])
  (:require [com.ashafa.clutch :as clutch :only[with-db get-view]]))

(def collection-media-type "application/vnd.cms.collection+json;version=1")

(defn create-collection
  "Create a new collection using the specified name and optional map of properties.
  If :slug is included in the properties it will be used as the collection's slug,
  otherwise one will be created from the name."
  ([db name] (create-collection db name  {}))
  ([db name props] (when-let [collection (common/create db (merge props {:name name}) :collection)]
                     (common/map-from-db collection))))

(defn get-collection
  "Given the slug of the collection, return the collection as a map, or nil if there's no collection with that slug"
  [slug db]
  (if-let [coll (:doc (first (clutch/get-view db "collection" :all {:key slug :include_docs true})))]
    (common/map-from-db coll)))

;; TODO delete the items as well
(defn delete-collection
  "Given the slug of the collection, delete it and all its contents and return :ok,
  or return :bad-collection if the collection slug is not good"
  [slug db]
  (if-let [id (clutch/with-db db
    (:id (first (clutch/get-view "collection" :all {:key slug :include_docs false}))))]
      (do (common/delete-by-id id) :ok)
      :bad-collection))

(defmacro with-collection
  "Given the slug of the collection, execute some code with the retrieved collection
  lexically scoped as collection, or return :bad-collection if the collection slug is no good"
  [coll-slug db & body]
  `(if-let [~'collection (get-collection ~coll-slug ~db)]
    (clutch/with-db ~db
      ~@body)
    :bad-collection))

(defn item-count
  "Given the slug of the collection, return the number of items it contains,
  or return :bad-collection if the collection slug is no good"
  [coll-slug db]
  (with-collection coll-slug db
    (if-let [result (first (clutch/get-view "item" :count-by-collection {:key (:id collection) :include_docs false}))]
      (:value result)
      0)))

(defn all-items [coll-slug])
(defn all-collections [])

(defn get-collections [db]
  (->> (clutch/get-view db "collection" :all)
       (map (juxt :key :id))
       (into {})
       keywordize-keys))

(defn get-all-documents [db]
  (->> (clutch/get-view db "cms" :all)
       (map (juxt :key :value))
       (into {})
       keywordize-keys))

(defn get-collection-items-by-id [coll-id db]
  (let [items (keywordize-keys (into {} (map (juxt :_id :data) (map #(get db %) (map :id (clutch/get-view db "item" :all))))))
        filtered-items (filter #(= coll-id (-> items % :collection)) (keys items))]
    (map #(get db (name %)) filtered-items)))

(defn get-collection-items [slug db]
  (get-collection-items-by-id (:id (get-collection slug db)) db))

(defn get-collection-item [db coll-slug slug]
  (let [items (get-collection-items coll-slug db)]
      (first (filter #(= slug (-> % :data :name)) items))))


(comment
  Each collection of links is a top level business area of operation

 The tree of link is restricted by the hierarchy applied to the collection

 * site layout and description
 * advertising
 * user account settings
 * business operation administration
 * network operation administration 
 * system operation administration
 * syndication
 * reporting

 A generic set of views traverse the link items in the
 various collections.  As it consumes the list builds a map.
 This data is the model for the various business operations

 The business areas of operation could have high level protocols
 that allow the reification of of objects.  These objects would
 have methods that use the retrieved model.

 )
