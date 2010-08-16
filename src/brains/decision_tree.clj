(ns brains.decision-tree
  (require [clojure.set :as set]
           [clojure.contrib.string :as str]
           [brains.data :as data]
           [brains.probability-map :as pm]))

(defn- log2 [p]
  (/ (Math/log p) (Math/log 2)))

(defn- entropy [p]
  (- (* p (log2 p))))

(defn- igain
  "Calculates information gain on selecting this feature."
  [pm feature]
  (println feature)
  (let [outcome-e (for [o (pm/outcomes pm)]
                   (entropy (pm/p-o pm o)))
        all-e (for [outcome (pm/outcomes pm)
                    value (pm/feature-values pm feature)
                    :let [p-v (pm/p-f pm [feature value])
                          p (pm/p-of pm outcome [feature value])]
                    :when p]
                (* p-v (entropy p)))]
    (println all-e)
    (- (apply + outcome-e)
       (apply + all-e))))

(defn- feature-branch
  "Returns data points with matching feature->value, with that feature removed"
  [{:keys [data]} feature value]
  (for [d data :when (= value (feature d))]
    (dissoc d feature)))

(defn- build-tree
  "Builds decision tree from supplied data set and probability map."
  [data pm]
  (cond
   (= 1 (count (pm/outcomes pm)))
   (:leaf (first (pm/outcomes pm)))

   (or (empty? (:data data)) (empty? (pm/features pm)))
   (:leaf (pm/most-common-outcome pm))

   :else
   (let [gains (sort-by #(igain pm %) (pm/features pm))
         best-f (last gains)
         rest-f (butlast gains)
         children (for [v (pm/feature-values pm best-f)]
                    (let [new-points (feature-branch data best-f v)
                          new-data (assoc data :data new-points)]
                      [v, (build-tree new-data (pm/build new-data))]))]
     [:node best-f (into {} children)])))

(defn train [pm data])
(defn main [outcome datafile]
  (let [outcome (keyword outcome)
        data (data/load-from-file outcome datafile)
        points (shuffle (:data data))
        [test train] (split-at (/ (count points) 4)
                               points)
        pm (pm/build (assoc data :data train))]
    (doall
     (for [f (pm/features pm)]
       (println (igain pm f)))
     (println (build-tree data pm)))))
