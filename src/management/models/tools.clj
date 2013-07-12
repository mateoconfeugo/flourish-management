(ns management.models.tools
  "Getting data to feed the various tools"
  (:require [korma.core :only [defentity database insert values has-one select* with select where]])        
  (:use [cheshire.core]
        [metis.core]
        [management.config :only[db-name db-password db-address db-user]]
        [management.models.orm-spec :as orm :only [user profile]]
        [management.views.snippets]
        [riemann.client :only [send-event tcp-client]]        
        [korma.db :only [defdb mysql]]
        [korma.core :only[select where with]]))

;;(defdb db (mysql {:db db-name :user db-user :password db-password :host db-address} ))
(defdb db (mysql {:db "mgmt" :user "root" :password "test123" :host "127.0.0.1"}))

(defn default-stats []
  {:clicks 31 :visits 20 :conversions 2})

(defn optimization-opportunities []
  {:clicks 31 :visits 20 :conversions 2})

(defn current-oppurtunities []
  {:clicks 31 :visits 20 :conversions 2})

(defn landing-site-stats []
  {:clicks 31 :visits 20 :conversions 2})

(defn get-profile-settings
  [user-data]
  (assoc user-data :tools {:dashboard {:description "The objects that make up the display upon login"
                                       :display-tuple [{:order 1
                                                        :display-name "Summary Statistics"
                                                        :gather-with  `default-stats
                                                        :display-with (quote management.views.snippets/default-stats-summary)}
                                                       {:order 2
                                                        :gather-with  `landing-site-stats
                                                        :display-name "Landing Site Data"
                                                        :display-with (quote management.views.snippets/landing-site-stats-summary)}]}
                           :landing-site {:dashboard
                                          {:description "The objects that make up the display upon login"
                                           :display-tuple [{:order 1
                                                            :display-name "Landing Site Stats" 
                                                            :gather-with (quote management.views.snippets/landing-site-stats)
                                                            :display-with (quote management.views.snippets/landing-site-stats-summary)}
                                                           {:order 2
                                                            :display-name "Opportunities"
                                                            :gather-with (quote management.views.snippets/optimization-opportunities)
                                                            :display-with (quote management.views.snippets/default-stats-summary)}]}}
                           :site-editor {:description "Work on existing or new sites"
                                         :display-tuple [{:order 1
                                                          :display-name "Site Editor"
                                                          :gather-with (quote management.views.snippets/landing-site-stats)
                                                          :display-with (quote management.views.snippets/landing-site-stats-summary)}
                                                         {:order 2
                                                          :display-name "Page Editor"
                                                          :gather-with (quote management.views.snippets/optimization-opportunities)
                                                          :display-with (quote management.views.snippets/default-stats-summary)}]}}))

;; TODO: Fix this with a function that assembles the menu from the features available 

(defn dashboard-config
  [{:keys [user-id] :as settings}]
   "The profile for the user determines what display components will be displayed.
    Each display object represents the top of chain of function calls through an mvc stack.
    Create a list of snippets to call all with the data to needed populate them"
   (let [user-data (first (select user (with profile) (where {:id user-id})))
         fake-menu-data (:drop_down_menu (parse-string (slurp (str (System/getProperty "/user.dir") "resources/fake_menu_data.json")) true))
         active-profile (get-profile-settings user-data) 
         snippet-functions (map :display-with (-> active-profile :tools :dashboard :display-tuple))
         gather-functions (map :gather-with (-> active-profile :tools :dashboard :display-tuple))
         display-names (map :display-name (-> active-profile :tools :dashboard :display-tuple))
         display-order (map :order (-> active-profile :tools :dashboard :display-tuple))         
         model-view-tuple (apply map (fn [x y z w] {:view x :model ((eval y)) :order z :display-name w}) [snippet-functions gather-functions display-order display-names])
         sorted-display-objects (sort-by :order model-view-tuple)]
     {:menu-data fake-menu-data :display-objects sorted-display-objects}))
