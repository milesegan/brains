(ns brains.classify
  (require brains.naive-bayes
           [clojure.contrib.string :as str]
           [brains.data :as data]
           [brains.probability-map :as pm]))

(def classifiers
     {"bayes" brains.naive-bayes/classify})

(defn main [method outcome datafile]
  (if-let [classifier (classifiers method)]
    (let [outcome (keyword outcome)
          dataset (data/load-from-file datafile)
          {:keys [data outcomes] :as dataset} (data/extract-field outcome :outcomes dataset)
          [test train] (data/shuffle-and-split dataset 4)
          pm (pm/build outcome train)
          classes (for [p (:data test)]
                    [(outcomes (:label p)) (classifier pm outcome p)])
          correct (filter (fn [[a b]] (= a b)) classes)]
      (doall
       (for [c classes] (println c)))
      (println (double
                (/ (count correct) (count (:data test))))))
    (println "unknown method" method)))


