(ns management.views.admin
  "Create a unordered list that will form the dom host for a bootstrap tabbed interface"
  (:use [net.cgrand.enlive-html]))

(deftemplate admin-dashboard "templates/admin_dashboard.html"
  [{:keys [display-map] :as settings}]
;;  [:div#navbar] (content (nav-bar {:title site-name :menu-data (:menu-data display-map)}))
  [:ul#nav-controls-destination :li] (clone-for [v (:views display-map)]
                                                [:a] (do->
                                                      (add-class "btn")
                                                      (add-class "btn-success")
                                                      (set-attr :href (str "#tab" (:order v)))
                                                      (set-attr :data-toggle "tab")
                                                      (html-content (:display-name v))))
  [:section.tab-content :div.tab-pane](clone-for [v (:views display-map)]
                                                 [:a] (do->                                                       
                                                       (set-attr :id (str "tab" (:order v)))
                                                       (html-content ((:snippet-function v) display-map))))
  [[:ul.pages (nth-of-type 1)] :> first-child] (add-class "active")
  [[:.tab-pane first-child]] (add-class "active"))
