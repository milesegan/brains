(ns test-probability-map
  (use clojure.test)
  (require [brains.probability-map :as pm]
           [brains.data :as data]))

(deftest test-build
  (let [dataset (data/load-from-file "data/house-votes.csv")
        dataset (data/extract-field :party :outcomes dataset)
        m (pm/build :party dataset)
        outcomes (pm/outcomes m)]
    (is (not-empty outcomes))
    (is (< 0 (pm/p-o m (first outcomes))))))
