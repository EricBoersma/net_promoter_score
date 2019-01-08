(ns net-promoter-score.nps-dates
  (:require [java-time :as jt]))

(defn convert-nps-objects-to-dates
  "Converts all string date values within a hash map to use java.time.LocalDate objects for easy filtering."
  ([values date-format]
   (map (fn [x] (update x :date (fn [d] (jt/local-date date-format d)))) values))
  ([values]
   (convert-nps-objects-to-dates values "yyyy-MM-dd")))

(defn date-between-dates
  "Checks whether a given date is between two dates"
  [given-date less-date greater-date]
  (and (or (= less-date given-date) (= less-date (jt/min given-date less-date)))
       (or (= greater-date given-date) (= greater-date (jt/max given-date greater-date)))))

(defn get-nps-score-objects-for-date-range
  "Gets a list of NPS score hashmaps for the provided date range. Scores should be stored in hashes of the form `{:score X :date Y}`
  Date may be of the type String or of type `java.time.LocalDate`. If the first instance of `:date` is not `java.time.LocalDate`,
  all entries in the range will be converted to `java.time.LocalDate` before being filtered. This method returns the entire score hashmap in a vector,
  in their original order. If you wish to retrieve just the scores, use `get-nps-scores-for-date-range.` If you know that the dates need to be parsed,
   the `date-format` parameter may be included. The default is `yyyy-MM-dd`."
  ([values start-date end-date]
   (get-nps-score-objects-for-date-range values start-date end-date "yyyy-MM-dd"))
  ([values start-date end-date date-format]
   (if (instance? java.time.LocalDate (:date (first values)))
     (filter #(date-between-dates (:date %) start-date end-date) values)
     (filter #(date-between-dates (:date %) start-date end-date)
             (convert-nps-objects-to-dates values date-format)))))

(defn get-nps-scores-for-date-range
  "Gets a list of NPS scores for the provided date range. Scores should be stored in hashes of the form `{:score X :date Y}`.
  Date may be of type String or of type `java.time.LocalDate`. If the first instance of `:date` is not `java.time.LocalDate`, all
  entries in the range will be converted to `java.time.LocalDate` before being filtered. This method returns only the score values
  in a vector, in their original order. If you wish to return the hash containing both the score and the date, use `get-nps-hashes-for-date-range.`
  If you know that the dates need to be parsed, the `date-format` parameter may be included. The default is `yyyy-MM-dd`."
  ([values start-date end-date]
   (get-nps-scores-for-date-range values start-date end-date "yyyy-MM-dd"))
  ([values start-date end-date date-format]
   (map :score
        (get-nps-score-objects-for-date-range values start-date end-date date-format))))

(defn get-standard-date-ranges
  "Gets a set of start/end date ranges for summary ranges of NPS score summaries. Takes a parameter of the anchor date,
  which defaults to today's date."
  ([]
   (get-standard-date-ranges (jt/local-date)))
  ([anchor-date]
   (hash-map :last-seven
     (hash-map :start-date (jt/minus anchor-date (jt/days 7)) :end-date anchor-date)
             :last-thirty
     (hash-map :start-date (jt/minus anchor-date (jt/days 30)) :end-date anchor-date)
             :thirty-to-sixty
     (hash-map :start-date (jt/minus anchor-date (jt/days 60)) :end-date (jt/minus anchor-date (jt/days 30)))
             :sixty-to-ninety
     (hash-map :start-date (jt/minus anchor-date (jt/days 90)) :end-date (jt/minus anchor-date (jt/days 60))))))

