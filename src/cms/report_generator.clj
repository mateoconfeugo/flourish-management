(defn generate-report
  "take query-results and vectors each with the following info:
   [header-display [key(s)] manipulation-function]
 
   key(s) is a vector of nested keywords. e.g. [:operator :first-name]
   which is passed to a get-in call
 
   manipulation-function is the function to apply to the item.
 
   returns a map with :headers and :rows"
  [query-results & report-items]
  (let [headers (vec (map first report-items))
        process (fn [ks f]
                  #(f (get-in % ks)))
        columns (map #(process (% 1) (% 2)) report-items)
        rows (for [r query-results]
               ((apply juxt columns) r))]
    {:headers headers
     :rows rows}))