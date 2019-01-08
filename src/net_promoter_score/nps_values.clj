(ns net-promoter-score.nps-values
  (:require [net-promoter-score.nps-validation :as validate]
            [clojure.math.numeric-tower :as math]))

(defn ^:private mean
  "Calculates the mean of a list of values, but does not validate that they conform to nps scoring requirements"
  [values]
  (double (/ (reduce + values) (count values))))

(defn nps-mean
  "Calculates the double-precision mean value of the given score, used to provide a summary of NPS scores over a period of time. Validates that the scores are valid NPS scores before calculation."
  [values]
  {:pre [(validate/in-nps-range? values)]}
  (if (empty? values) "N/A" (mean values)))

(defn find-middle-vector-elements
  "Finds the middle elements in a vector, useful for calculating the median value."
  [element-count]
  {:pre (> element-count 0)}
  (case element-count
    1 [0]
    2 [0 1]
    [(int (Math/floor (/ element-count 2)))
     (dec (int (Math/floor (/ element-count 2))))]))

(defn nps-median
  "Calculates the median value of the provided NPS scores, used to provide a summary of NPS scores over a period of time."
  [values]
  {:pre [(validate/in-nps-range? values)]}
  (if (empty? values)
    "N/A"
    (let [sorted-vals            (sort values)
          value-count            (count values)
          middle-vector-elements (find-middle-vector-elements value-count)]
      (if (= 0 (mod (count sorted-vals) 2))
        ; If the number of items is even, then get the mean of the two middle values.
        (nps-mean
         [(nth sorted-vals (first middle-vector-elements))
          (nth sorted-vals (second middle-vector-elements))])
        ; if the number of items is odd, get the middle value
        (nth sorted-vals (first middle-vector-elements))))))

(defn nps-variance
  "Calculates the population variance of a provided set of NPS scores, used to provide a summary of scores over a period of time."
  [values]
  {:pre [(validate/in-nps-range? values)]}
  (if (empty? values)
    "N/A"
    (let [mean-value (nps-mean values)]
      (mean (map #(math/expt (- % mean-value) 2) values)))))

(defn nps-standard-deviation
  "Calculates the population standard deviation of a provided set of NPS scores, used to provide a summary of scores over a period of time."
  [values]
  {:pre [(validate/in-nps-range? values)]}
  (if (empty? values)
    "N/A"
    (let [mean-value (nps-mean values)]
      (math/sqrt (nps-variance values)))))