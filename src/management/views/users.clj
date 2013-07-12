(ns management.views.users
  (:use [hiccup.core :only (h)]
        [hiccup.form :only (form-to label text-area submit-button text-field)])
  (:require [management.views.layout :as layout]))

(defn user-form []
  [:div {:id "user-form" :class "sixteen columns alpha omega"}
    (form-to [:post "/"]
      (label "First Name" "What do you want to USER?")
      (text-field "fname")
      (text-field "lname")      
      (submit-button "Create"))])

(defn display-users [users]
  [:div {:id "users sixteen columns alpha omega"} (map (fn [user] [:h2 {:class "user"} (str (:fname user) (:lname user))])  users)]
  )

(defn index [users]
  (layout/common "USER"
    (user-form)
    [:div {:class "clear"}]
    (display-users users)))

(defn users-list [users]
  (layout/common "List"
    (display-users users)))
