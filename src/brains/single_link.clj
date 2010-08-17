(ns brains.clustering.single-link
  (use brains.clustering.util)
  (require [clojure.contrib.seq :as seq]))

(def mdistance (memoize distance))

(defn build-clusters [d points clusters]
  (if (empty? points)
    clusters
    (let [p (first points)
          others (rest points)
          is-close? #(> d (mdistance
                           (:values p) (:values %)))
          parts (seq/separate is-close? others)
          [close far] parts]
      (recur d far (cons (cons p close)
                         clusters)))))

(defn cluster [k points]
  (loop [clusters points
         distance 0]
    (let [oldcount (count clusters)
          newclusters (build-clusters distance points nil)
          newcount (count newclusters)]
      (if (= newcount k)
        newclusters
        (recur newclusters (+ distance 1))))))

