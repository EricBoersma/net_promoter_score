(ns net-promoter-score.nps-calc)

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

