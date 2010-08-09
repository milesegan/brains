(ns brains.data
  (:require [clojure.contrib.string :as string]))

(defn- build-point [fields values]
  {:label (first values)
   :values (apply hash-map (interleave (rest fields)
                                       (rest values)))})

(defn load-set [path]
  (let [lines (line-seq (-> path (java.io.FileReader.) (java.io.BufferedReader.)))
        lines (map #(string/split #",\s?" %) lines)
        attrs (map #(keyword (string/lower-case %))
                   (first lines))]
    (map #(build-point attrs %)
         (rest lines))))
