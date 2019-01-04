(ns net-promoter-score.nps-calc
  (:require [net-promoter-score.nps-validation :refer :all]))

(defn calculate-percentile-value
  "Calcualtes the percentile value (X out of 100) for given detractor/passive/generator value"
  [value total]
  {:pre [(> value -1) (pos? total) (>= total value)]}
  (int (* (/ value total) 100)))

(defn calculate-net-promoter-score
  "Takes detractors/passives/generators and provides a raw net promoter score"
  [detractors passives generators]
  {:pre [(> detractors -1) (> passives -1) (> generators -1)]}
  (let [total (+ detractors passives generators)]
    (- (calculate-percentile-value generators total)
       (calculate-percentile-value detractors total))))

(defn find-all-detractors
  "Finds all scores classified as a detractor by net promoter score rankings.
  If `maximum` value is provided, then finds all scores less than or equal provided value. Default maximum is 6."
  ([values maximum]
   {:pre [(in-nps-range values)]}
   (filter #(<= % maximum) values))
  ([values]
   {:pre [(in-nps-range values)]}
   (find-all-detractors values 6)))

(defn find-all-promoters
  "Finds all scores classified as a promoter by net promoter score rankings.
  If `minimum` value is provided, then finds all scores greater than or equal to provided value. Default minimum is 9."
  ([values minimum]
   {:pre [(in-nps-range values)]}
   (filter #(>= % minimum) values))
  ([values]
   {:pre [(in-nps-range values)]}
   (find-all-promoters values 9)))

(defn find-all-passives
  "Finds all scores classified as a passive by net promoter score rankings.
  If `minimum` and `maximum` values are provided, then finds all scores greater than (not equal) and less than (not equal) the minimum and maximum values, respectively.
  Default minimum is 6, default maximum is 9."
  ([values minimum maximum]
   {:pre [(in-nps-range values)]}
   (filter #(< minimum % maximum) values))
  ([values]
   {:pre [(in-nps-range values)]}
   (find-all-passives values 6 9)))

