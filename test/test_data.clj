(ns test-data
  (use clojure.test)
  (require [brains.data :as data]))

(deftest test-load
  (testing "load a file"
    (let [dataset (data/load-from-file "data/house-votes.csv")
          dataset (data/extract-field :party :outcomes dataset)]
      (is (some #{:crime} (:fields dataset)))
      (is (= 435 (count (:outcomes dataset))))
      (is (= 435 (count (:data dataset)))))))
