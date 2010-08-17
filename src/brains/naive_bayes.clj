(ns brains.naive-bayes
  (require [clojure.contrib.string :as str]
           [brains.data :as data]
           [brains.probability-map :as pm]))

(defn- bayesian [pm outcome fv]
  (let [pOF (pm/p-of pm outcome fv)
        pO (pm/p-o pm outcome)
        pF (pm/p-f pm fv)]
    (if (and pOF pO pF)
      (/ (* pOF pO) pF)
      (/ 1 (* 10 (:count pm))))))

(defn- bayesian-product [pm outcome d]
  (reduce * (for [fv (:values d)]
              (bayesian pm outcome fv))))

(defn classify [pm outcome p]
  (let [outcomes (sort-by #(bayesian-product pm % p)
                          (pm/outcomes pm))]
    (last outcomes)))


