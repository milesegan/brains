(ns brains.clustering
  (:require [clojure.string :as string]
            [brains.clustering.kmeans :as kmeans]
            [brains.clustering.single-link :as single-link]))

(defn- load-points [path]
  (let [lines (line-seq (-> path (java.io.FileReader.) (java.io.BufferedReader.)))
        lines (map #(string/split % #", ") (rest lines))]
    (for [line lines]
      {:label (first line)
       :values (map #(Double. ^String %) (rest line))})))

(defn- do-cluster [fn k path]
  (let [clusters (fn (Integer. k) (load-points path))]
    (doseq [c clusters]
      (println (map :label c)))))

(defn do-kmeans [k path]
  (do-cluster kmeans/cluster k path))

(defn do-single-link [k path]
  (do-cluster single-link/cluster k path))

