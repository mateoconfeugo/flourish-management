(ns management.controllers.tools
  "Dispatch to the different top level tool dashboards that make up the management user interface
   Each databoard is a webapp with host dom and corresponding javascript application"
  (:use [compojure.core :only [defroutes GET ANY]]
        [management.views.tools :only [user-dashboard]]
        [management.views.snippets]        
        [management.models.tools]))
        
(defroutes user-mgmt-routes
    (GET"/mgmt/user/:id" [id] (user-dashboard {:display-map (dashboard-config {:user-id 1})})))
;;  (GET "/mgmt/user/:id" [id] (user-dashboard (dashboard-config {:user-id id}))))