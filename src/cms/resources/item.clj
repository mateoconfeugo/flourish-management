(ns cms.resources.item
  (:require [com.ashafa.clutch :as clutch]
            [cms.resources.common :as common]
            [cms.resources.collection :as collection]))

(def item-media-type "application/vnd.cms.item+json;version=1")

(defn item-doc [db coll-id item-slug]
  (:doc (first (clutch/get-view db "item" :all {:key [coll-id, item-slug] :include_docs true}))))

(defn- item-from-db 
  "Turn an item from its CouchDB map representation into its CMS map representation."
  [coll-slug item]
  (common/map-from-db (assoc-in item [:data :collection] coll-slug)))

(defn create-item
  "Create a new item in the collection specified by its slug, using the specified
  item name and an optional map of properties. If :slug is included in the properties
  it will be used as the item's slug, otherwise the slug will be created from
  the name."
  ([db coll-slug item-name] (create-item db coll-slug item-name {}))
  ([db coll-slug item-name props]
     (let [collection (collection/get-collection coll-slug db)]
       (when-let [item (common/create-with-db db (:name props) coll-slug (merge props {:collection (:id collection) :name (str item-name)}) :item)]
        (item-from-db coll-slug item)))))

(defn get-item
  "Given the slug of the collection containing the item and the slug of the item,
  return the item as a map, or return :bad-collection if there's no collection with that slug, or
  nil if there is no item with that slug."
  [db coll-slug item-slug]
  (let [collection (collection/get-collection coll-slug db)]  
    (when-let [item (item-doc db (:id collection) item-slug)]
      (item-from-db coll-slug item))))

;;(get-item "test" "bar")



(defn delete-item
  "Given the slug of the collection containing the item and the slug of the item,
  delete the item, or return :bad-collection if there's no collection with that slug, or
  :bad-item if there is no item with that slug."
  [coll-slug item-slug]
  (if-let [coll-id (:id (collection/get-collection coll-slug))]
    (if-let [item (clutch/with-db (common/db) (item-doc coll-id item-slug))]
      (:ok (common/delete item))
      :bad-item)
    :bad-collection))

(defn valid-new-item?
  "Given the slug of the collection, and a map of a potential new item,
  check if the everything is in order to create the new item.
  Ensure the collection exists, the name of the item is specified,
  and the slug is valid and doesn't already exist if it's specified."
  ([coll-slug item-name] (valid-new-item? coll-slug item-name {}))
  ([coll-slug item-name {provided-slug :slug}]
    (if-let [coll-id (:id (collection/get-collection coll-slug))]
      (cond
        (not item-name) :no-name
        (not provided-slug) :OK
        (not (common/valid-slug? provided-slug)) :invalid-slug
        (not (get-item coll-slug provided-slug)) :OK
        :else :slug-conflict)
      :bad-collection)))

(defn valid-item-update?
  ""
  ([coll-slug item-slug {item-name :name provided-slug :slug}]
    (if-let [coll-id (:id (collection/get-collection coll-slug))]
      (cond
        (not item-name) :no-name
        (not provided-slug) :OK
        (not (common/valid-slug? provided-slug)) :invalid-slug
        :else :OK)
      :bad-collection)))

(defn link
  "Adds an existing item to the business category that represents  business collection
   linked together and contrained by the business system heierarcy"
  [{:keys [child collection  heirarchy] :as props}]
  (let [parent (:parent child)]
    (if (isa? heirarchy child parent)
      (create-item collection {:parent-type (:type parent)
                               :type (:type child)
                               :parent-uri (:uri parent)
                               :uri (:uri child)})
      nil)))

(defn add
  "Adds a new item to the business category where the actual instance of the
   data doc is and to business collection linked together and contrained by
   the heierarcy"
  [{:keys [child parent collection heirarchy] :as settings}]
  (let [item (create-item (:type child) child)]
    (if (and (contains? item :type) (isa? heirarchy child parent))
      (do
        (link child collection heirarchy)
        (item))
      nil)))

