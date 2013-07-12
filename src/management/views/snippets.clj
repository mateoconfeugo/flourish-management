(ns management.views.snippets
  (:use [net.cgrand.enlive-html :as html]))

(def ^:dynamic *default-stats-sel* [:section#default-stats])
(defsnippet default-stats-summary "templates/default_stats_summary.html" *default-stats-sel*
  [model]
  [:tr :td] (content "foo"))
  
(def ^:dynamic *landing-site-sel* [:section#default-stats])
(defsnippet  landing-site-stats-summary "templates/landing_site_stats_summary.html" *landing-site-sel*
  [model])
;;  [:tr :td] (content "bar"))

(defsnippet site-nav-header "templates/site-nav-header.html" [:nav.container-fluid]
  [site-name]
  [:a.brand] (content site-name))


;;menu items
(def ^:dynamic  *menu-item-sel* [[:ul.dropdown-menu (nth-of-type 1)] :> first-child])
(defsnippet menu-item-model "templates/site-nav-header.html" *menu-item-sel*
  [item]
  [:a] (do->
        (content (:menu_item_text item))
        (set-attr :href (:menu_item_url item))))

(def ^:dynamic  *menu-sel* [:li.dropdown])
(defsnippet menu-model "templates/site-nav-header.html" *menu-sel*
  [{:keys [drop_down_menu_name  menu_item]} model]
  [:.menu-header]   (content drop_down_menu_name)
  [:ul.dropdown-menu] (content (map model  menu_item)))

(def ^:dynamic *nav-bar-sel* [:nav.container-fluid])
(defsnippet nav-bar  "templates/site-nav-header.html" *nav-bar-sel*
  [{:keys [title menu-data]}]
  [:a.brand] (content title)
  [:ul#nav-bar-dropdown-menu] (html/content (map #(menu-model % menu-item-model)  menu-data)))

