(ns brains.pmap
  (require [clojure.contrib.string :as str])
  (import java.io.BufferedReader
          java.io.FileReader))

(def fieldsep #"\s*,\s*")

(defn- inc-map [map path]
  (assoc-in map path (+ 1 (get-in map path 0))))

(defn- build [outcome fields data p-f p-o p-of]
  (if (empty? data)
    [p-f p-o p-of]
    (let [p (first data)
          o (outcome p)
          vals (dissoc p outcome)]
      (recur outcome fields (rest data)
             (reduce inc-map p-f vals)
             (inc-map p-o o)
             (reduce inc-map p-of
                     (for [[f v] vals]
                       [f v o]))))))

(defn- make-point [fields vals]
  (zipmap (rest fields) (rest vals)))
      
(defn main [outcome path]
  (let [lines (->> path
                   (FileReader.)
                   (BufferedReader.)
                   line-seq
                   (map #(str/replace-re #"^(#|\s).*" "" (str/trim %)))
                   (filter not-empty)
                   (map #(str/split fieldsep %)))
        fields (map keyword (first lines))
        data (for [p (rest lines)]
               (make-point fields p))]
    (println (build outcome fields data {} {} {}))))

(main (keyword
       (nth *command-line-args* 1))
      (nth *command-line-args* 2))
