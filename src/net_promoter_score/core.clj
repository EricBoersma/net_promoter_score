(ns net-promoter-score.core
  (:require [net-promoter-score.nps-calc :as nps-calc]
            [net-promoter-score.nps-values :as nps-values]
            [net-promoter-score.nps-validation :as validate]
            [net-promoter-score.nps-dates :as nps-dates]
            [java-time :as jt]))

(defn get-promoter-score
  "Takes a list of ratings from a NPS survey and provides the raw Net Promoter Score. If passive-minimum and passive-maximum are
  provided, those wil be used to configure calculations for bounding detractor/passive/promoter scores. Defaults are 6 and 9
  for minimum and maximum."
  ([values]
   {:pre [(validate/in-nps-range? values)]}
   (get-promoter-score values 6 9))
  ([values passive-minimum passive-maximum]
   {:pre [(validate/in-nps-range? values)
          (> passive-maximum passive-minimum)]}
   (if (empty? values)
     "N/A"
     (nps-calc/calculate-net-promoter-score
      (count (nps-calc/find-all-detractors values passive-minimum))
      (count (nps-calc/find-all-passives values passive-minimum passive-maximum))
      (count (nps-calc/find-all-promoters values passive-maximum))))))

(defn get-promoter-summary
  "Takes a list of ratings from a NPS survey and provides a summary of those values, including the raw promoter score.
  If passive-minimum and passive-maximum are provided, those wil be used to configure calculations for bounding
  detractor/passive/promoter scores. Defaults are 6 and 9 for minimum and maximum."
  ([values]
   (get-promoter-summary values 6 9))
  ([values passive-minimum passive-maximum]
   {:pre [(validate/in-nps-range? values)
          (> passive-maximum passive-minimum)]}
   (if (empty? values)
     (hash-map
      :score              "N/A"
      :median             "N/A"
      :mean               "N/A"
      :standard-deviation "N/A")
     (hash-map
      :score              (get-promoter-score values passive-minimum passive-maximum)
      :median             (nps-values/nps-median values)
      :mean               (nps-values/nps-mean values)
      :standard-deviation (nps-values/nps-standard-deviation values)))))

(defn get-promoter-summary-with-dates
  "Takes a list of ratings from a NPS survey, including the dates those ratings were saved, then provides a summary of scores for
  all time, last 7 days, last 30 days, 30-60 day and 60-90 day windows. If passive-minimum and passive-maximum are provided,
  those wil be used to configure calculations for bounding detractor/passive/promoter scores. Defaults are 6 and 9 for minimum and maximum."
  ([values]
   (get-promoter-summary-with-dates values (jt/local-date)))
  ([values anchor-date]
   (get-promoter-summary-with-dates values 6 9 anchor-date))
  ([values passive-minimum passive-maximum anchor-date]
   (let [date-ranges (nps-dates/get-standard-date-ranges anchor-date)]
     (hash-map :all-dates
       (get-promoter-summary
        (map :score values)
        passive-minimum passive-maximum)
               :last-seven
       (get-promoter-summary
        (nps-dates/get-nps-scores-for-date-range values (:start-date (:last-seven date-ranges)) (:end-date (:last-seven date-ranges)))
        passive-minimum passive-maximum)
               :last-thirty
       (get-promoter-summary
        (nps-dates/get-nps-scores-for-date-range values (:start-date (:last-thirty date-ranges)) (:end-date (:last-thirty date-ranges)))
        passive-minimum passive-maximum)
               :thirty-to-sixty
       (get-promoter-summary
        (nps-dates/get-nps-scores-for-date-range values (:start-date (:thirty-to-sixty date-ranges))
                                                 (:end-date (:thirty-to-sixty date-ranges)))
        passive-minimum passive-maximum)
               :sixty-to-ninety
       (get-promoter-summary
        (nps-dates/get-nps-scores-for-date-range values (:start-date (:sixty-to-ninety date-ranges))
                                                 (:end-date (:sixty-to-ninety date-ranges)))
        passive-minimum passive-maximum)))))
