(ns brains.clustering.dendrogram)

(defn add [dgram d clusters]
  (cons [d clusters] dgram))

(defn new [points]
  [[0 (map #(cons % nil) points)]])
