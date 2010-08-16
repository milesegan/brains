(ns brains.data
  (require [clojure.contrib.string :as str])
  (import java.io.BufferedReader
          java.io.FileReader))

(def fieldsep #"\s*,\s*")

(defn load-from-file [outcome path]
  (let [lines (->> path
                   (FileReader.)
                   (BufferedReader.)
                   line-seq
                   (map #(str/replace-re #"^(#|\s).*" "" (str/trim %)))
                   (filter not-empty)
                   (map #(str/split fieldsep %)))
        fields (map keyword (first lines))]
    {:fields fields
     :outcome outcome
     :data (for [p (rest lines)]
             (zipmap (rest fields) (rest p)))}))

