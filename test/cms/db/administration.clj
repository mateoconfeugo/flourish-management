(ns cms.db.administration
  "Test adding new user so they can author landing sites"
  (:refer-clojure :exclude [assoc! conj! dissoc! name parents replace reverse delete-document])
  (:require  [clojure.walk :refer (keywordize-keys)] )
  (:use [clojure.core :only [parents]]
        [clojure.string :as str]
        [cms.db.administration]
        [cms.db.views]
        [cms.db.schemas.authoring]
        [cms.document :only [children child?]]
        [com.ashafa.clutch]
        [com.ashafa.clutch.http-client]
        [cms.resources.item :only [create-item get-item item-doc]]
        [cms.resources.common :as common]
        [cms.resources.collection :as collection]
        [expectations]))

;; TEST: Creating a user database in document store for customized authored content and settings

(cms.db.views/init-db "ralph" false)
(def user-db (common/get-user-database "ralph"))
(def authoring-schema (setup-user-data-resources "ralph" false))
(expect true (= "gusto-cms-ralph" (:db_name (database-info user-db))))

(def test-user {:username "test_user_one" :status :new})
(def test-domain {:name "bar.com" :status :new})
(def test-landing-site {:id 1 :status :new})
                
;; TEST creating a new user able to create landing sites for a domain
(def authoring-profile-doc (add-new-authoring-user {:db user-db
                                                    :user test-user
                                                    :domain test-domain
                                                    :landing-site test-landing-site}))

(defn create-landing-site
  [{:keys [id landing-site] :as args}]
  {:id id})

(defn create-market-vector
  [{:keys [id landing-site] :as args}]
  {:id id
   :landing-site [(create-landing-site args)]})

(defn create-domain
  [{:keys [domain-name market-vector] :as args}]
  {:domain-name domain-name
   :market-vector [(create-market-vector args)]})

(defn create-user
  [{:keys [name domain] :as args}]
  {:username name
   :domain [(create-domain args)]})

(def lm {:1 :2
         :2 :3
         :3 :4
         :4 nil})

(def li {:1 "one"
         :2 "two"
         :3 "three"
         :4 "four"})

(defn create-link-map [doc]
  (let [links (:entity-links doc)
        pairs (map #(rest %) (map #(split %  #"\/") (map :uri links)))
        relations (map (juxt :uri :parent-uri) links)
        children-fn (fn [x] (split (nth x 0) #"\/"))
        parents-fn (fn [x] (if-let [r (nth x 1)] (split r #"\/") nil))
        c (map #(rest %)(map #(children-fn %) relations))
        p (map #(rest %)(map #(parents-fn %) relations))
        index (zipmap c p)]
    index))

(defn composition-map [doc]
  (let [links (:entity-links doc)
        pairs (map #(rest %) (map #(split %  #"\/") (map :uri links)))
        relations (map (juxt :uri :parent-uri) links)
        children-fn (fn [x] (split (nth x 0) #"\/"))
        parents-fn (fn [x] (if-let [r (nth x 1)] (split r #"\/") nil))
        c (map #(rest %)(map #(children-fn %) relations))
        p (map #(rest %)(map #(parents-fn %) relations))
        index (zipmap p c)]
    index))

(defn build-index [db link-map]
  (apply hash-map (apply concat (map #(list (key %) (collection/get-collection-item db  (first (key %)) (nth (key %) 1))) link-map))))

(defn ancestry [m k]
  (if (contains? m k)
    (cons k (ancestry  m (m k)))
    k))

(comment
(defn create-key-sequence [link-map link-index link]
  (let [ks (into [] (remove #(nil? %) (reverse (ancestry link-map link link-index))))]
    (if (= 1 (count ks)) [] ks))))

(defn create-key-sequence [link-map  link]
  (into [] (remove #(nil? %) (reverse (ancestry link-map link)))))
  


(defn update-model
  [model coll-key-seq item]
  (let [item-key (first (keys item))
        item-val (first (vals item))
        item-key-seq (conj coll-key-seq item-key)
        existing-data (if-let [iks (get-in model item-key-seq)] iks nil)]
    (cond (nil? existing-data) (assoc-in model coll-key-seq item)
          (vector? existing-data) (update-in model item-key-seq #(conj % item-val))
          (get-in model item-key-seq) (update-in model item-key-seq #(conj [%] item-val))                    
          (map? existing-data) (update-in model coll-key-seq #(merge % item))
          :else (assoc-in model coll-key-seq item))))

(defn populate-model
  [link-map remaining  link-index  model]
  (if (not (empty? remaining))
    (let [link (first remaining)
          ks (create-key-sequence link-map link)
          item (get link-index (nth link 0))
          updated (update-model model ks item)]
      (recur link-map (dissoc remaining (nth link 0)) link-index updated))
    model))

(def lm {:4 nil
         :3 :4
         :2 :3
         :1 :2})

(def li {:1 {:d "one"}
         :2 {:c "two"}
         :3 {:b "three"}
         :4 {:a "four"}})

(create-key-sequence lm :3)
(populate-model lm lm li {})

(create-key-sequence

  [link-map link-index  model]
  (if (not (empty? lm))
(def  link (first  lm))
(def ks (create-key-sequence lm li link))
(def item (get li (nth link 0)))
(def updated (update-model {} ks item))
(dissoc lm (nth link 0))

      (recur (dissoc link-map link) link-index updated))
    model))


(keys {})



(def lm (create-link-map authoring-profile-doc))
(def cm (composition-map authoring-profile-doc))
(def li (build-index user-db lm))

  (let [key-seq (create-key-sequence link-map link-index link)
        new-child-entity (get link-index link)
        existing-child-entity (get-in model key-seq)]



(def bar (update-model {} [:4 :3] {:name "big"}))
(def foo (update-model bar [:4 :3] {:name "hank"}))
(def ham (update-model foo [:4 :3] {:name "ralph"}))
(def beans (update-model ham [:4 :3] {:age 24}))
(def pickle (update-model beans [:4] {:3 24}))
(def radish (update-model pickle [] {:blah 1}))
(def apple (update-model radish [] {:blah 2}))

(def root (first (first (filter #(empty? (val %)) lm))))
(def 
(filter  #(= (first child) (val %)) lm)
(def h  authoring-heirarchy)
(def entity-types (map  #(nth (key %) 0) index))
(def top (first (filter #(= nil (parents h (keyword %))) entity-types)))
(def children (children h (keyword top)))
        pairs (map #(rest %) (map #(split %  #"\/") (map :uri links)))]

(first (map #(list (nth (val %) 0) (collection/get-collection-item user-db  (first (key %)) (nth (key %) 1))) index))
(map #(list (keyword (nth (key %) 0)) (collection/get-collection-item user-db  (first (key %)) (nth (key %) 1))) index)
(def kids [])
(apply hash-map (apply concat (map #(list (keyword (nth (val %) 0)) (collection/get-collection-item user-db  (first (key %)) (nth (key %) 1))) index)

(parents h :landing-site)                       
(def 

(reduce (fn [m [k & vals]] (assoc m k (conj (m k) vals))) {} lol)
(map #(last (val %))  index)

{:user {
        :domain [{:landing-site [{:id 
                  :name "foo.com"}}]
        
 
(def original [1 '(a b c) 2])
(require '[clojure.zip :as zip])
(def root-loc (zip/seq-zip (seq original)))
(zip/node (zip/down root-loc))
(-> root-loc zip/down zip/right zip/node)
(-> root-loc zip/down zip/right zip/down zip/right zip/node)
(def b (-> root-loc zip/down zip/right zip/down zip/right))
(zip/lefts b)
(zip/rights b)
(zip/path b)
(def loc-in-new-tree (zip/remove (zip/up b)))
(zip/root loc-in-new-tree)
(zip/node loc-in-new-tree)
(zip/node (zip/insert-right loc-in-new-tree "z"))
(zip/path loc-in-new-tree)

(defn print-tree [original]
  (loop [loc (zip/seq-zip (seq original))]
    (if (zip/end? loc)
      (zip/root loc)
      (recur (zip/next
              (do (println (zip/node loc))
                  loc))))))
(print-tree b)

  


(flatten (keys (zipmap children parents)))

(split (nth (first relations) 0) #"\/")

(map #(rest %) (map #(split %  #"\/") (map :uri relations)))
  


(def landing-site (collection/get-collection-item user-db "landing-site" 1))
(def domain (collection/get-collection-item user-db "domain" "bar.com"))
(def user (collection/get-collection-item user-db "user" "test_user_one"))
(expect true (= (-> user :data :username) (:username test-user)))
(expect true (= (-> domain :data :name) (:name test-domain)))
(expect true (= (-> landing-site :data :id) (:id test-landing-site)))
(delete-database user-db)
