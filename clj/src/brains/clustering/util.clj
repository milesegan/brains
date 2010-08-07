(ns brains.clustering.util)

(defn distance [a b]
  (let [squares (map (fn [x y] (Math/pow (- x y) 2))
                     a b)]
    (Math/sqrt (apply + squares))))
