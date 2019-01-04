(ns net-promoter-score.core
  (:require [net-promoter-score.nps-calc :refer :all])
  (:require [net-promoter-score.nps-values :refer :all])
  (:require [net-promoter-score.nps-validation :refer :all]))

(defn get-promoter-score
  "Takes a list of ratings from a NPS survey and provides the raw Net Promoter Score. If passive-minimum and passive-maximum are
  provided, those wil be used to configure calculations for bounding detractor/passive/promoter scores. Defaults are 6 and 9
  for minimum and maximum."
  ([values]
   {:pre [(in-nps-range values) (not-empty values)]}
   (get-promoter-score values 6 9))
  ([values passive-minimum passive-maximum]
   {:pre [(in-nps-range values) (not-empty values) (> passive-maximum passive-minimum)]}
   (calculate-net-promoter-score
    (count (find-all-detractors values passive-minimum))
    (count (find-all-passives values passive-minimum passive-maximum))
    (count (find-all-promoters values passive-maximum)))))

(defn get-promoter-summary
  "Takes a list of ratings from a NPS survey and provides a summary of those values, including the raw promoter score.
  If passive-minimum and passive-maximum are provided, those wil be used to configure calculations for bounding
  detractor/passive/promoter scores. Defaults are 6 and 9 for minimum and maximum."
  ([values]
   (get-promoter-summary values 6 9))
  ([values passive-minimum passive-maximum]
   {:pre [(in-nps-range values) (not-empty values) (> passive-maximum passive-minimum)]}
   (hash-map :score (get-promoter-score values passive-minimum passive-maximum) :median (nps-median values) :mean (nps-mean values))))
