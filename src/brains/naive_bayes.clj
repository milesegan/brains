(ns brains.naive-bayes
  (require [clojure.contrib.string :as str]
           [brains.data :as data]
           [brains.probability-map :as pm]))

(defn bayesian [pm outcome fv]
  (let [pOF (pm/p-of pm outcome fv)
        pO (pm/p-o pm outcome)
        pF (pm/p-f pm fv)]
    (if (and pOF pO pF)
      (/ (* pOF pO) pF)
      (/ 1 (* 10 (:count pm))))))

(defn classify [pm outcome p]
  (let [probs (for [o (pm/outcomes pm)]
                [o
                 (reduce * (map #(bayesian pm o %)
                                (dissoc p outcome)))])]
    (ffirst (reverse (sort-by last probs)))))

(defn main [outcome datafile]
  (let [outcome (keyword outcome)
        data (data/load-from-file outcome datafile)
        points (shuffle (:data data))
        [test train] (split-at (/ (count points) 4)
                               points)
        pm (pm/build (assoc data :data train))
        classes (for [p test]
                  [(outcome p) (classify pm outcome p)])
        correct (filter (fn [[a b]] (= a b)) classes)]
    (println (double
              (/ (count correct) (count test))))))
