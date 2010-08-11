(ns brains.classification.naive-bayes
  (:use brains.data))

(defn- inc-map
  "Increments the count corresponding to kv in map by one."
  [map kv]
  (assoc map kv (+ 1 (get map kv 0))))

(defn- build-counts
  "Builds maps of counts I, C and C|I."
  [data p-i p-c p-ci]
  (if (empty? data)
    [p-i p-c p-ci]
    (let [p (first data)
          c (:concept p)]
      (recur (rest data)
             (reduce inc-map p-i (:values p))
             (inc-map p-c c)
             (reduce (fn [m i]
                       (assoc m i (assoc (m i)
                                    c (+ 1 (get-in m [i c] 0)))))
                     p-ci
                     (:values p))))))

(defn- count-to-prob
  "Converts a map of keys to counts to keys to probabilities."
  [m]
  (let [sum (apply + (vals m))]
    (into {} (for [[k v] m]
               [k (double (/ v sum))]))))

(defn- build-probs
  "Builds maps of probabilities of I, C, and C|I."
  [data]
  (let [[p-i p-c p-ci] (build-counts data {} {} {})]
    {:count (count data)
     :p-i (count-to-prob p-i)
     :p-c (count-to-prob p-c)
     :p-ci (into {} (for [[i cc] p-ci]
                      [i (count-to-prob cc)]))}))

(defn- probability
  "Returns the probability that point p belongs to concept c given probs."
  [c p probs]
  (let [ps (for [i (:values p)]
             (let [p-ci (get-in probs [:p-ci i c])
                   p-c (get-in probs [:p-c c])
                   p-i (get-in probs [:p-i i])]
               (if p-ci
                 (/ (* p-ci p-c) p-i)
                 (/ 0.1 (:count probs)))))] ;; default for unseen instance
    (apply * ps)))

(defn classify
  "Classifies the point according to probs."                
  [point probs]
  (let [concepts (keys (:p-c probs))
        c-probs (for [c concepts]
                  [c (probability c point probs)])]
    (-> (sort-by last c-probs)
        last
        first)))

(defn main [concept-key path]
  (let [data (load-set path)
        concept-key (keyword concept-key)
        mapped-data (for [d data]
                      (assoc d
                        :concept (get (:values d) concept-key)
                        :values (dissoc (:values d) concept-key)))
        [test-data training-data] (split-at (/ (count mapped-data) 3)
                                            mapped-data)]
    (let [probs (build-probs training-data)
          classes (for [p test-data]
                    [(classify p probs) (:concept p)])
          correct (count (filter (fn [[a b]] (= a b))
                                 classes))
          percent (/ correct (count test-data))]
      (doseq [p classes]
        (println p))
      (println (double percent)))))

(defn time-main [concept-key path]
  (time (main concept-key path)))

