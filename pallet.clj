;;; Pallet project configuration file

;;; By default, the pallet.api and pallet.crate namespaces are already referred.
;;; The pallet.crate.automated-admin-user/automated-admin-user us also referred.

;;; (require '[your-ns :refer [your-group]])

(defproject management
  :provider {:vmfest
             {:node-spec
              {:image {:image-id "java-mysql-postfix"}}
              :selectors #{:default}}})
