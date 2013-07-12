(ns cms.site-builder.dummy-snippet
  (:use [cms.site-builder :only[field-item-model process-elements]]))

(def uuid 1)

(def ^:dynamic *uuid-sel* (str ".uuid_" uuid))
  
(defsnippet uuid-name  "uuid/uuid.html" *uuid-sel*
  [model]
  (let [data-selector (determine-deconstructor uuid)
        data (-> model data-selector)]
    [*uuid-sel*] (if (contains? data :elements)
                   (do
                     (process-elements (:elements data))
                     (map field-item-model (:fields element)))
                   (content (map #(field-item-mode {:item-key %1 :item-value ((keyword %1) data)}) (keys data))))))
