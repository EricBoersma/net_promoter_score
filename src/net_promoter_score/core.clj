(ns net-promoter-score.core
  (:require [net-promoter-score.nps-calc :as nps-calc])
  (:require [net-promoter-score.nps-values :as nps-values])
  (:require [net-promoter-score.nps-validation :as validate]))

(defn get-promoter-score
  "Takes a list of ratings from a NPS survey and provides the raw Net Promoter Score. If passive-minimum and passive-maximum are
  provided, those wil be used to configure calculations for bounding detractor/passive/promoter scores. Defaults are 6 and 9
  for minimum and maximum."
  ([values]
   {:pre [(validate/in-nps-range? values) (not-empty values)]}
   (get-promoter-score values 6 9))
  ([values passive-minimum passive-maximum]
   {:pre [(validate/in-nps-range? values) (not-empty values) (> passive-maximum passive-minimum)]}
   (nps-calc/calculate-net-promoter-score
    (count (nps-calc/find-all-detractors values passive-minimum))
    (count (nps-calc/find-all-passives values passive-minimum passive-maximum))
    (count (nps-calc/find-all-promoters values passive-maximum)))))

(defn get-promoter-summary
  "Takes a list of ratings from a NPS survey and provides a summary of those values, including the raw promoter score.
  If passive-minimum and passive-maximum are provided, those wil be used to configure calculations for bounding
  detractor/passive/promoter scores. Defaults are 6 and 9 for minimum and maximum."
  ([values]
   (get-promoter-summary values 6 9))
  ([values passive-minimum passive-maximum]
   {:pre [(validate/in-nps-range? values) (not-empty values) (> passive-maximum passive-minimum)]}
   (hash-map :score (get-promoter-score values passive-minimum passive-maximum) :median (nps-values/nps-median values) :mean (nps-values/nps-mean values))))
