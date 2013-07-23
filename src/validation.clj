(ns validation)

(defmacro assert* [val test]
  `(let [result# ~test]
     (when (not result#)
       (throw (Exception.
               (str "Test failed: " (quote ~test)
                    " for " (quote ~val) " = " ~val))))))

(defmulti validate* (fn [val test] test))

(defmethod validate* :non-zero [x _]
  (assert* x (not= x 0)))

(defmethod validate* :even [x _]
  (assert* x (even? x)))

(defmethod validate* :exists? [x _]
  (assert* x (resolve 'x)))

(defn validate [& tests]
  (doseq [test tests] (apply validate* test)))

;;(defn divide [x y]
;;  (validate [y :non-zero] [x :even])
;;  (/ x y))