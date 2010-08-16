(ns test-probability-map
  (use clojure.test)
  (require [brains.probability-map :as pm]
           [brains.data :as data]))

(deftest test-build
  (let [data (data/load-from-file :party "data/house-votes.csv")
        m (pm/build data)
        outcomes (pm/outcomes m)]
    (is (not-empty outcomes))
    (is (< 0 (pm/p-o m (first outcomes))))))
