(ns cms.api.items
  (:require [compojure.core :refer (defroutes ANY GET)]
            [liberator.core :refer (defresource)]
            [clojure.core.match :refer (match)]
            [taoensso.timbre :refer (debug info warn error fatal spy)]
            [cms.api.common :as common]
            [cms.resources.collection :as collection]
            [cms.resources.common :as resources]            
            [cms.resources.item :as item]
            [cms.representations.items :refer (render-item render-items)]))

(defn- get-item [coll-slug item-slug]
  (let [item (item/get-item coll-slug item-slug)]
    (match item
      :bad-collection [false {:bad-collection true}]
      nil false
      :else {:item item})))

(defn get-collection [coll-slug]
  (if-let [collection (collection/get-collection coll-slug (resources/db))]
    [true {:collection collection}]
    [false {:bad-collection true}]))

(defn- item-location-response [coll-slug item]
  (common/location-response [coll-slug (:slug item)] (render-item item) item/item-media-type))

;; Create new item

(defn- check-new-item [coll-slug item]
  (let [reason (item/valid-new-item? coll-slug (:name item) item)]
    (if (= reason :OK)
      true
      [false {:reason reason}])))

(defn- create-item [coll-slug item]
  (when-let [item (item/create-item coll-slug (:name item) item)]
    {:item item}))

(defn- unprocessable-reason [reason]
  (match reason 
    :bad-collection common/missing-collection-response
    :no-name "Name is required."
    :slug-conflict "Slug already used in collection."
    :invalid-slug "Invalid slug."
    else ))

;; Update item

(defn- check-item-update [coll-slug item-slug item]
  (let [reason (item/valid-item-update? coll-slug item-slug item)]
    (if (= reason :OK)
      true
      [false {:reason reason}])))

;; Resources, see: http://clojure-liberator.github.io/liberator/assets/img/decision-graph.svg

(defresource item [coll-slug item-slug]
  :available-charsets [common/UTF8]
  :available-media-types [item/item-media-type]
  :handle-not-acceptable (fn [ctx] (common/only-accept item/item-media-type))
  :allowed-methods [:get :put :delete]
  :exists? (fn [ctx] (get-item coll-slug item-slug))
  :handle-not-found (fn [ctx] (when (:bad-collection ctx) common/missing-collection-response))
  ;; Get an item
  :handle-ok (fn [ctx] (render-item (:item ctx)))
  ;; Delete an item
  :delete! (fn [ctx] (item/delete-item coll-slug item-slug))
  ;; Update/Create an item
;;  :malformed? (fn [ctx] (common/malformed-json? ctx)) 
  :processable? (fn [ctx] (check-item-update coll-slug item-slug (:data ctx)))
  :can-put-to-missing? (fn [_] false) ; temporarily only use PUT for update
  :conflict? (fn [ctx] false)
  :put! (fn [ctx] (spy "HERE: put!"))
  :handle-not-implemented (fn [ctx] (when (:bad-collection ctx) common/missing-collection-response))
  :handle-created (fn [ctx] (item-location-response coll-slug (:item ctx))))

(defresource items-list [coll-slug]
    :available-charsets [common/UTF8]
    :available-media-types [item/item-media-type]
    :handle-not-acceptable (fn [ctx] (common/only-accept item/item-media-type))
    :allowed-methods [:get :post]
    :exists? (fn [ctx] (get-collection coll-slug))
    ;; Get list of items  
    :handle-ok (fn [ctx] (render-items (:items ctx)))
    ;; Create new item
    :known-content-type? (fn [ctx] (=  (get-in ctx [:request :content-type]) item/item-media-type))
;;    :malformed? (fn [ctx] (common/malformed-json? ctx)) 
    :processable? (fn [ctx] (check-new-item coll-slug (:data ctx)))
    :handle-unprocessable-entity (fn [ctx] (unprocessable-reason (:reason ctx)))
    :post! (fn [ctx] (create-item coll-slug (:data ctx)))
    :handle-created (fn [ctx] (item-location-response coll-slug (:item ctx))))

;; (render-item (get-item "test" "bar"))
;;(render-item (get-collection "test"))
(render-items (get-collection "test"))
(cms.resources.collection/get-collection-items "test2" (resources/db))
(cms.resources.collection/get-collection-items-by-id "b2af465016ab5dcba6fe2f22c100a51d"  (resources/db))

;; Routes

(defroutes item-routes
  (ANY "/:coll-slug/:item-slug" [coll-slug item-slug] (item coll-slug item-slug))
  (ANY "/:coll-slug/" [coll-slug] (items-list coll-slug)))
