(ns brains.clustering.single-link
  (use brains.clustering.util)
  (require [brains.clustering.dendrogram :as dgram]
           [clojure.contrib.seq :as seq]))

(defn build-clusters [d points]
  (loop [points points
         clusters nil]
    (if (empty? points)
      clusters
      (let [p (first points)
            others (rest points)
            parts (seq/separate #(> d (distance (:values p) (:values %)))
                                others)
            [close far] parts]
        (recur far (cons (cons p close) clusters))))))

(defn cluster [k points]
  (loop [clusters points
         distance 0
         d (dgram/new clusters)]
    (let [oldcount (count clusters)
          newclusters (build-clusters distance points)
          newcount (count newclusters)
          newdgram (if (not= oldcount newcount)
                     (dgram/add d distance newclusters)
                     d)]
      (if (= newcount k)
        newdgram
        (recur newclusters (+ distance 1) newdgram)))))
