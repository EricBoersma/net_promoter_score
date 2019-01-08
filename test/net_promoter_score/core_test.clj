(ns net-promoter-score.core-test
  (:require [clojure.test :refer :all]
            [net-promoter-score.core :as nps-core]
            [java-time :as jt]))

(defn ^:private get-random-detractor-values
  "Gets a list of the specified number of detractor values (less than 7)"
  [count source]
  (into source (take count (repeatedly #(rand-int 7)))))

(defn ^:private get-random-passive-values
  "Gets a list of the specified number of passive values (7 and 8)"
  [count source]
  (into source (take count (repeatedly #(+ (rand-int 2) 7)))))

(defn ^:private get-random-promoter-values
  "Gets a list of the specified number of promoter values (9 and 10)"
  [count source]
  (into source (take count (repeatedly #(+ (rand-int 2) 9)))))

(def ^:private anchor-date (jt/local-date "1990-01-01"))

(defn ^:private get-dated-hash-vector
  "Gets a vector of dated NPS score hashmaps"
  []
  (map #(hash-map :score % :date anchor-date) (range 1 11)))

(deftest get-promoter-score-test
  (testing "Correctly calculates the NPS based on some large number of values"
           (is
             (= 0
                (nps-core/get-promoter-score
                  (->> [] (get-random-detractor-values 10) (get-random-passive-values 10) (get-random-promoter-values 10)))))
           (is
             (= 66
                (nps-core/get-promoter-score
                  (->> [] (get-random-detractor-values 10) (get-random-passive-values 10) (get-random-promoter-values 70)))))
           (is
             (= 70
                (nps-core/get-promoter-score
                  (->> [] (get-random-detractor-values 100) (get-random-passive-values 100) (get-random-promoter-values 800)))))
           (is
             (= -70
                (nps-core/get-promoter-score
                  (->> [] (get-random-detractor-values 8000) (get-random-passive-values 1000) (get-random-promoter-values 1000)))))
           (is (= -40 (nps-core/get-promoter-score [1 2 3 4 5 6 7 8 9 10]))))

  (testing "Correctly calculates the NPS when provided custom min/max values"
           (is (= 0 (nps-core/get-promoter-score [1 2 3 4 5 6 7 8 9 10] 4 7))))

  (testing "Correctly validates provided function parameters"
           (is (thrown? AssertionError (nps-core/get-promoter-score [-1 0 1])))
           (is (thrown? AssertionError (nps-core/get-promoter-score [1 2 3 4 5] 7 3))))

  (testing "Correctly returns 'N/A' when provided an empty list"
           (is (= "N/A" (nps-core/get-promoter-score [])))))

(deftest get-promoter-summary-test
  (testing "Correctly provides a summary of net promoter score."
           (is
             (= {:score -40 :mean 5.5 :median 5.5 :standard-deviation 2.8722813232690143}
                (nps-core/get-promoter-summary [1 2 3 4 5 6 7 8 9 10]))))

  (testing "Correctly provides a summary when given custom min/max values"
           (is
             (= {:score 0 :mean 5.5 :median 5.5 :standard-deviation 2.8722813232690143}
                (nps-core/get-promoter-summary [1 2 3 4 5 6 7 8 9 10] 4 7))))

  (testing "Correctly validates provided function parameters"
           (is (thrown? AssertionError (nps-core/get-promoter-summary [-1 0 1])))
           (is (thrown? AssertionError (nps-core/get-promoter-summary [1 2 3 4 5] 7 3))))

  (testing "Correctly handles empty lists"
           (is
             (= {:score "N/A" :mean "N/A" :median "N/A" :standard-deviation "N/A"}
                (nps-core/get-promoter-summary [])))))

(deftest get-promoter-summary-with-dates-test
  (testing "Correctly provides a summary of items from all time"
           (is
             (= {:score -40 :mean 5.5 :median 5.5 :standard-deviation 2.8722813232690143}
                (:all-dates (nps-core/get-promoter-summary-with-dates (get-dated-hash-vector))))))

  (testing "Correctly provides a summary of items from the previous seven days"
           (is
            (= {:score -40 :mean 5.5 :median 5.5 :standard-deviation 2.8722813232690143}
               (:last-seven (nps-core/get-promoter-summary-with-dates (get-dated-hash-vector) anchor-date)))))

  (testing "Correctly provides a summary of items from the previous thirty days"
           (is
            (= {:score -40 :mean 5.5 :median 5.5 :standard-deviation 2.8722813232690143}
               (:last-thirty (nps-core/get-promoter-summary-with-dates (get-dated-hash-vector) anchor-date)))))

  (testing "Correctly provides a summary of items for 30-60 day window"
           (is
            (= {:score -40 :mean 5.5 :median 5.5 :standard-deviation 2.8722813232690143}
               (:thirty-to-sixty (nps-core/get-promoter-summary-with-dates (get-dated-hash-vector) (jt/plus anchor-date (jt/days 31)))))))

  (testing "Correctly provides a summary of items for 30-60 day window"
           (is
            (= {:score -40 :mean 5.5 :median 5.5 :standard-deviation 2.8722813232690143}
               (:sixty-to-ninety (nps-core/get-promoter-summary-with-dates (get-dated-hash-vector) (jt/plus anchor-date (jt/days 61))))))))
