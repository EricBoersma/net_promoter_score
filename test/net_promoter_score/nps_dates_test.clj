(ns net-promoter-score.nps-dates-test
  (:require [clojure.test :refer :all]
            [net-promoter-score.nps-dates :as nps-dates]
            [java-time :as jt]))

(deftest convert-nps-objects-to-dates-test
  (testing "Correctly converts a list of hashes with a date key to use date objects"
           (is
            (=
             [{:date (jt/local-date "1990-01-01")}
              {:date (jt/local-date "1990-01-02")}
              {:date (jt/local-date "1990-12-31")}]
             (nps-dates/convert-nps-objects-to-dates
              [{:date "1990-01-01"} {:date "1990-01-02"} {:date "1990-12-31"}]))))

  (testing "Correctly converts a list of hashes with a date key to use date objects with non-standard string formatting"
           (is
            (=
             [{:date (jt/local-date "1990-01-01")}
              {:date (jt/local-date "1990-01-02")}
              {:date (jt/local-date "1990-12-31")}]
             (nps-dates/convert-nps-objects-to-dates
              [{:date "01-01-1990"} {:date "01-02-1990"} {:date "12-31-1990"}] "MM-dd-yyyy"))))

  (testing "Includes the rest of the hashmap when converting the date"
           (is
            (=
             [{:date (jt/local-date "1990-01-01") :score 10}
              {:date (jt/local-date "1990-01-02") :score 9}
              {:date (jt/local-date "1990-12-31") :score 7}]
             (nps-dates/convert-nps-objects-to-dates
              [{:date "1990-01-01" :score 10}
               {:date "1990-01-02" :score 9}
               {:date "1990-12-31" :score 7}])))))

(deftest date-between-dates-test
  (testing "Correctly finds that a date is between two encapuslating dates"
           (is
            (= true
               (nps-dates/date-between-dates (jt/local-date "1990-01-01") (jt/local-date "1989-12-31") (jt/local-date "1990-02-01")))))
  (testing "Correctly finds that a date is not between two non-encapuslating dates"
           (is
            (= false
               (nps-dates/date-between-dates (jt/local-date "1989-12-31") (jt/local-date "1990-01-01") (jt/local-date "1990-02-01"))))))

(deftest get-nps-score-objects-for-date-range-test
  (testing "Correctly filters objects that already contain date objects in the date key"
           (is
            (= [{:date (jt/local-date "1990-01-01") :score 10}]
               (nps-dates/get-nps-score-objects-for-date-range
                [{:date (jt/local-date "1990-01-01") :score 10}
                 {:date (jt/local-date "1991-01-01") :score 0}
                 {:date (jt/local-date "1989-01-01") :score 10}]
                (jt/local-date "1989-12-31") (jt/local-date "1990-02-01")))))

  (testing "Correctly filters objects that contain strings as objects in the date key"
           (is
            (= [{:date (jt/local-date "1990-01-01") :score 10}]
               (nps-dates/get-nps-score-objects-for-date-range
                [{:date "1990-01-01" :score 10}
                 {:date "1991-01-01" :score 0}
                 {:date "1989-01-01" :score 10}]
                (jt/local-date "1989-12-31") (jt/local-date "1990-02-01"))))))

(deftest get-nps-score-for-date-range-test
  (testing "Correctly gets the filtered scores that are within the listed range"
           (is
            (= [2 1 3]
               (nps-dates/get-nps-scores-for-date-range
                [{:date "1990-01-02" :score 2}
                 {:date "1990-01-01" :score 1}
                 {:date "1990-01-03" :score 3}
                 {:date "1991-01-01" :score 0}
                 {:date "1989-01-01" :score 10}]
                (jt/local-date "1989-12-31") (jt/local-date "1990-02-01"))))))