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




(defn  transform-cms-xml
  "this function creates the data element hierachy to render the component as well as edit the make up of it"
  [xml])

(defn save-to-cms
  "move the entity data structure into cms database"
  []

(def base-element-schema {:name nil
                          :type (base )
                          :fields [{:name nil :type nil :contents nil}]
                          :elements []})

(def base-element-editor-schema {:name nil
                          

(defprotocol Element
  "base composite data operations for deeply nested "
  (get-elements [this elements]
    "Gets all the subelements ")
  (get-fields [this element]
    "Retrieves the fields associated with the element"))

(defprotocol CMS-Element
  "E"

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


  


  