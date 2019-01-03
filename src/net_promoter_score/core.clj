(ns net-promoter-score.core
  (:require [net-promoter-score.nps-calc :refer :all]))

(defn get-promoter-score
  "Takes a list of ratings from a NPS survey and provides the raw Net Promoter Score"
  [values]
  {:pre [(in-nps-range values)]}
  (calculate-net-promoter-score (count (find-all-detractors values)) (count (find-all-passives values)) (count (find-all-promoters values))))
