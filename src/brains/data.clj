(ns brains.data
  (require [clojure.contrib.string :as str])
  (import java.io.BufferedReader
          java.io.FileReader))

(def fieldsep #"\s*,\s*")

(defn extract-field
  "Extracts all field from all points in dataset, storing them in a new
attribute keyed as key, hashed by the label of each point."
  [field key dataset]
  (let [{data :data} dataset
        labels (map :label data)
        values (for [d data]
                 (-> d :values field))
        newvalues (for [d data]
                    (assoc d :values
                           (dissoc (:values d) field)))
        extract (zipmap labels values)]
    (assoc dataset
      key extract
      :data newvalues)))

(defn shuffle-and-split
  "Splits a dataset by factor, returning two sets."
  [dataset factor]
  (let [data (shuffle (:data dataset))
        [a b] (split-at (/ (count data)
                           factor)
                        data)]
    [(assoc dataset :data a)
     (assoc dataset :data b)]))
            
(defn load-from-file
  "Loads a dataset from the given path."
  [path]
  (let [lines (->> path
                   (FileReader.)
                   (BufferedReader.)
                   line-seq
                   (map #(str/replace-re #"^(#|\s).*" "" (str/trim %)))
                   (filter not-empty)
                   (map #(str/split fieldsep %)))
        fields (map keyword (first lines))
        data (for [p (rest lines)]
                 {:label (first p)
                  :values (zipmap (rest fields) (rest p))})]
    {:fields fields :data data}))

