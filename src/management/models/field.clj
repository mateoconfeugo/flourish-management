(ns management.models.element
  (:require [clojure.java.jdbc :as sql]))

(defn all [db-conn]
  (sql/with-connection db-conn
    (sql/with-query-results results
      ["select * from user order by id desc"]
      (into [] results))))

(defn create [user db-conn]
  (sql/with-connection db-conn
    (let [fname (:first-name user)
          lname (:last-name user)]
      (sql/insert-values :user [:fname :lname] [fname lname]))))

(def base-field {:name nil :type nil :contents nil})

(defn- save-field-dispatch
  [field]
  (let type

(defmulti save-field
  "take a field map and save it"
  (fn [field] (:type field)))

(defn- field-editor-dispatch
  [field]
  ((eval (str (:type field) "-editor"))))

(defmulti display field-editor
  "Display the field editor with in the context of the larger element editor"
  #'field-editor-dispatch
  :defaul nil)

(defsnippet text-box-editor snippet-path *field-selector*
  [{:keys [keyname label default-value min-occurence max-occurence position size max-size])

(defsnippet radio-button-editor snippet-path *field-selector*
  [])

(defsnippet checkbox-editor snippet-path *field-selector*
  [])

(defsnippet pulldown-editor snippet-path *field-selector*
  [])

(defsnippet select-editor snippet-path *field-selector*
  [])

(defsnippet code-select-editor snippet-path *field-selector*
  [])

(defsnippet date-editor snippet-path *field-selector*
  [])

(defsnippet text-box snippet-path *field-selector*
  [{:keys [keyname label default-value min-occurence max-occurence position size max-size])

(defsnippet radio-button snippet-path *field-selector*
  [])

(defsnippet checkbox snippet-path *field-selector*
  [])

(defsnippet pulldown snippet-path *field-selector*
  [])

(defsnippet select snippet-path *field-selector*
  [])

(defsnippet code-select snippet-path *field-selector*
  [])

(defsnippet date snippet-path *field-selector*
  [])




(defn  transform-cms-xml
  "this function creates the data element hierachy to render the component as well as edit the make up of it"
  [xml])

(defn save-to-cms
  "move the entity data structure into cms database"
  []



;; IMPLEMENTATION CONSTRUCTOR
(defn new-element
  [{:keys[element-type fields elements] :as settings}]
      (reify CMS-Site
        ;; PROTOCOL METHOD IMPLEMENTATIONS    
        (get-site-contents [this token]
          (let [options {:base-path base-path :rel-path site-cfg-path :site-tag site-tag :token token}
                site-id (get-site-id options)
                page-files (assemble-site-files base-path site-tag site-id)]
          (populate-contents base-path site-cfg-path site-tag page-files token ))))))


  


  