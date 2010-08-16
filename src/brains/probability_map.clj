(ns brains.probability-map
  (require [clojure.contrib.string :as str]
           [brains.data :as data]))

(defn- inc-map [map path]
  (assoc-in map path (+ 1 (get-in map path 0))))

(defn build [{:keys [outcome fields data]}]
  (let [c (count data)]
    (loop [data data p-f {} p-o {} p-of {}]
      (if (empty? data)
        {:count c :p-f p-f :p-o p-o :p-of p-of}
        (let [p (first data)
              o (outcome p)
              vals (dissoc p outcome)]
          (recur (rest data)
                 (reduce inc-map p-f vals)
                 (inc-map p-o [o])
                 (reduce inc-map p-of
                         (for [[f v] vals]
                           [f v o]))))))))

(defn- to-prob [pm p]
  (if p
    (/ p (:count pm))))

(defn feature-values [pm f]
  (keys (f (:p-f pm))))

(defn features [pm]
  (keys (:p-f pm)))

(defn p-o [pm outcome]
  (to-prob pm (get (:p-o pm) outcome)))

(defn p-of [pm outcome [f v]]
  (to-prob pm (get-in (:p-of pm) [f v outcome])))

(defn p-f [pm fv]
  (to-prob pm (get-in (:p-f pm) fv)))

(defn outcomes [pm]
  (keys (:p-o pm)))

(defn most-common-outcome [pm]
  (last (sort-by #(p-o pm %)
                 (outcomes pm))))

