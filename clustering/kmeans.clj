#!/usr/bin/env clj

(ns clustering
  (require [clojure.string :as string]))

(defn- read-file [path]
  (let [lines (line-seq (-> path (java.io.FileReader.) (java.io.BufferedReader.)))
        lines (map #(string/split % #", ") (rest lines))]
    (for [line lines]
      {:label (first line)
       :values (map #(Double. ^String %) (rest line))})))

(defn- centroid [points]
  (let [vals (map :values points)
        n (count (first vals))
        sums (for [i (range n)]
               (apply + (map #(nth % i) vals)))]
    (map #(/ % (count points)) sums)))

(defn- distance [a b]
  (let [squares (map (fn [x y] (Math/pow (- x y) 2))
                     a b)]
    (Math/sqrt (apply + squares))))

(defn- closest-centroid [p centroids]
  (let [distances (map-indexed (fn [i c]
                                 [(distance c (:values p)) i])
                               centroids)]
    (nth (first (sort distances)) 1)))

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

(let [k (Integer. (nth *command-line-args* 1))
      points (read-file (nth *command-line-args* 2))
      clusters (cluster k points)]
  (doseq [c clusters]
    (println (map :label c))))                          
