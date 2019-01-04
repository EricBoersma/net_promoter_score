(ns net-promoter-score.nps-validation)

(defn in-nps-range
  "Checks whether the provided list of numbers are in the acceptable NPS range (0-10)"
  [values]
  (every? #(<= 0 % 10) values))