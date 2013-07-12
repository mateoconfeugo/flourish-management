(ns cms.views.host-dom
  (:use [flourish-common.web-page-utils :only [run-server render-to-response render-request
                                               maybe-content maybe-substitute page-not-found]]
        [net.cgrand.enlive-html]))

(deftemplate site-builder-editor "templates/cms/builder.html"
  [{:keys [] :as settings}])

(defn render [req]
  "Serve up the hosting dom for the site builder editor"
  (render-to-response (site-builder-editor req)))

