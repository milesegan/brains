(ns brains.clustering.single-link
  (use brains.clustering.util)
  (require [clojure.contrib.seq :as seq]))

(def mdistance (memoize distance))

(defn build-clusters [d points]
  (loop [points points
         clusters nil]
    (if (empty? points)
      clusters
      (let [p (first points)
            others (rest points)
            parts (seq/separate #(> d (mdistance (:values p) (:values %)))
                                others)
            [close far] parts]
        (recur far (cons (cons p close) clusters))))))

(defn cluster [k points]
  (loop [clusters points
         distance 0]
    (let [oldcount (count clusters)
          newclusters (build-clusters distance points)
          newcount (count newclusters)]
      (if (= newcount k)
        newclusters
        (recur newclusters (+ distance 1))))))

