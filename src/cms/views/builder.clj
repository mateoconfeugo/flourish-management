(ns cms.views.builder
  (:use [flourish-common.web-page-utils :only [render-to-response]]
        [net.cgrand.enlive-html]))

(defsnippet top-nav "templates/cms/builder/navigation_bar.html" [:div.navbar-fixed-top] [])
(deftemplate site-builder-editor "templates/cms/builder.html"   [model])

;;  [:div.navbar-fixed-top] (top-nav (:nav :top model))

(defn render [req]
  "Serve up the hosting dom for the site builder editor"
  (render-to-response (site-builder-editor req)))

