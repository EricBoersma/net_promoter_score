(ns net-promoter-score.core
  (:require [net-promoter-score.nps-calc :refer :all])
  (:require [net-promoter-score.nps-values :refer :all])
  (:require [net-promoter-score.nps-validation :refer :all]))

(defn get-promoter-score
  "Takes a list of ratings from a NPS survey and provides the raw Net Promoter Score"
  ([values]
   {:pre [(in-nps-range values) (not-empty values)]}
   (calculate-net-promoter-score (count (find-all-detractors values)) (count (find-all-passives values)) (count (find-all-promoters values))))
  ([values passive-minimum passive-maximum]
   {:pre [(in-nps-range values) (not-empty values)]}
   (calculate-net-promoter-score (count (find-all-detractors values)) (count (find-all-passives values)) (count (find-all-promoters values)))))

(defn get-promoter-summary
  "Takes a list of ratings from a NPS survey and provides a summary of those values, including the raw promoter score"
  [values]
  {:pre [(in-nps-range values) (not-empty values)]}
  ({:score  (get-promoter-score values)
    :median (nps-median values)
    :mean   (nps-mean values)}))
