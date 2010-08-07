(ns brains.clustering.kmeans
  (require [brains.clustering.util :as util]))
  
(defn- centroid [points]
  (let [vals (map :values points)
        n (count (first vals))
        sums (for [i (range n)]
               (apply + (map #(nth % i) vals)))]
    (map #(/ % (count points)) sums)))

(defn- closest-centroid [p centroids]
  (let [distances
        (map-indexed (fn [i c]
                       [(util/distance c (:values p)) i])
                     centroids)
        [d closest] (first (sort distances))]
    closest))

(defn- initial-centroids [k points]
  (map :values (take k (shuffle points))))

(defn cluster [k points]
  (loop [centroids (initial-centroids k points)]
    (let [clusters (group-by #(closest-centroid % centroids) points)
          new-centroids (for [c (range k)]
                          (centroid (clusters c)))]
      (if (= centroids new-centroids)
        (vals clusters)
        (recur new-centroids)))))
