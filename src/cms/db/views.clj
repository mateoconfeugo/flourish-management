(ns cms.db.views
  (:refer-clojure :exclude [assoc! conj! dissoc! name parents]) ; suppress the shadowing warning  
  (:require [clojure.core :as core])
  (:use [com.ashafa.clutch :as clutch]
        [cms.resources.common :as common]))
(comment
  ;; http://localhost:5984/gusto-cms/_design/cms/_view/all
  ;; http://localhost:5984/gusto-cms/_design/collection/_view/all?include_docs=true
  ;; http://localhost:5984/gusto-cms/_design/cms/_view/all?key="id"
  ;; http://localhost:5984/gusto-cms/_design/cms/_view/all-by-type
  ;; http://localhost:5984/gusto-cms/_design/collection/_view/all-by-type?include_docs=true
  ;; http://localhost:5984/gusto-cms/_design/cms/_view/all-by-type?key=["type", "id"]
  ;; http://localhost:5984/gusto-cms/_design/collection/_view/all
  ;; http://localhost:5984/gusto-cms/_design/collection/_view/all?include_docs=true
  ;; http://localhost:5984/gusto-cms/_design/collection/_view/all?key="collection-slug"
  ;; http://localhost:5984/gusto-cms/_design/item/_view/all
  ;; http://localhost:5984/gusto-cms/_design/collection/_view/all?include_docs=true
  ;; http://localhost:5984/gusto-cms/_design/collection/_view/all?key=["collection-id", "item-slug"]
  ;; http://localhost:5984/gusto-cms/_design/item/_view/count-by-collection?key="collection-id"
  ;; http://localhost:5984/gusto-cms/_design/taxonomy/_view/all
  ;; http://localhost:5984/gusto-cms/_design/collection/_view/all?include_docs=true
  ;; http://localhost:5984/gusto-cms/_design/collection/_view/all?key="taxonomy-slug"
  )

(defn init [username]
  (let [db (common/create-user-database username)
        db-name (:db_name (database-info db))]
    (clutch/with-db db-name
      (clutch/save-view "cms" (clutch/view-server-fns :cljs
                                                      {:all {:map (fn [doc]
                                                                    (if-let [data (aget doc "data")]
                                                                      (if-let [type (aget data "type")]
                                                                        (if (#{"collection" "item" "taxonomy"} type)
                                                                          (js/emit (aget doc "_id") (aget data "type"))))))}
                                                       :all-by-type {:map (fn [doc]
                                                                            (if-let [data (aget doc "data")]
                                                                              (if-let [type (aget data "type")]
                                                                                (if (#{"collection" "item" "taxonomy"} type)
                                                                                  (js/emit (js/Array (aget data "type") (aget doc "_id")) nil)))))}}))
      (clutch/save-view "collection"
                        (clutch/view-server-fns :cljs {
                                                       :all {:map (fn [doc]
                                                                    (let [data (aget doc "data")]
                                                                      (when (and data (= (aget data "type") "collection"))
                                                                        (js/emit (aget data "slug") (aget doc "_id")))))}}))
      (clutch/save-view "item"
                        (clutch/view-server-fns :cljs {
                                                       :all {:map (fn [doc]
                                                                    (let [data (aget doc "data")]
                                                                      (when (and data (= (aget data "type") "item"))
                                                                        (js/emit (js/Array (aget data "collection") (aget data "slug")) (aget doc "_id")))))}
                                                       :all-by-collection {:map (fn [doc]
                                                                                  (let [data (aget doc "data")]
                                                                                    (when (and data (= (aget data "type") "item"))
                                                                                      (js/emit (aget data "collection") (aget data "slug")))))}
                                                       :count-by-collection {
                                                                             :map (fn [doc]
                                                                                    (let [data (aget doc "data")]
                                                                                      (when (and data (= (aget data "type") "item"))
                                                                                        (js/emit (aget data "collection") 1))))
                                                                             :reduce (fn [_ values _] (reduce + values))}}))
      (clutch/save-view "taxonomy"
                        (clutch/view-server-fns :cljs {
                                                       :all {:map (fn [doc]
                                                                    (let [data (aget doc "data")]
                                                                      (when (and data (= (aget data "type") "taxonomy"))
                                                                        (js/emit (aget data "slug") (aget doc "_id")))))}})))))

;; TODO want something more like below, so with less repition, but type needs to be a string literal by the time it makes it into the generated JS
;; at a minimum, need a macro that takes care of the with-db save-view view-server-fns boilerplate.

                                        ; (def cms-types #{"collection" "item" "taxonomy"})

                                        ; (defn- type-of [doc type]
                                        ;   (and (aget doc "data") (= (aget (aget doc "data") "type") type)))

                                        ; (defn all [type]
                                        ;   (clutch/with-db (common/db)
                                        ;     (clutch/save-view type-name
                                        ;       (clutch/view-server-fns :cljs {:all {:map (fn [doc]
                                        ;         (if (type-of doc type)
                                        ;           (js/emit (str (aget (aget doc "data") "slug") " | " (aget doc "_id") " | " (aget doc "_rev")) nil))}}))))

                                        ; (println "CMS: Initializing CouchDB views.")
                                        ; (doseq [type ["collection", "item", "taxonomy"]] (all type))
                                        ; (println "CMS: CouchDB view initialization complete.")
