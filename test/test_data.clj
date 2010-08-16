(ns test-data
  (use clojure.test)
  (require [brains.data :as data]))

(deftest test-load
  (testing "load a file"
    (let [data (data/load-from-file :party "data/house-votes.csv")]
      (is (= :party (:outcome data)))
      (is (some #{:crime} (:fields data)))
      (is (= 435 (count (:data data)))))))
