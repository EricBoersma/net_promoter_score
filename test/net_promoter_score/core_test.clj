(ns net-promoter-score.core-test
  (:require [clojure.test :refer :all]
            [net-promoter-score.core :refer :all]))

(defn get-random-detractor-values
  "Gets a list of the specified number of detractor values (less than 7)"
  [count source]
  (into source (take count (repeatedly #(rand-int 7)))))

(defn get-random-passive-values
  "Gets a list of the specified number of passive values (7 and 8)"
  [count source]
  (into source (take count (repeatedly #(+ (rand-int 2) 7)))))

(defn get-random-promoter-values
  "Gets a list of the specified number of promoter values (9 and 10)"
  [count source]
  (into source (take count (repeatedly #(+ (rand-int 2) 9)))))

(deftest get-promoter-score-test
  (testing "Correctly calculates the NPS based on some large number of values"
           (is (= 0 (get-promoter-score (->> [] (get-random-detractor-values 10) (get-random-passive-values 10) (get-random-promoter-values 10)))))
           (is (= 66 (get-promoter-score (->> [] (get-random-detractor-values 10) (get-random-passive-values 10) (get-random-promoter-values 70)))))
           (is (= 70 (get-promoter-score (->> [] (get-random-detractor-values 100) (get-random-passive-values 100) (get-random-promoter-values 800)))))
           (is (= -70 (get-promoter-score (->> [] (get-random-detractor-values 8000) (get-random-passive-values 1000) (get-random-promoter-values 1000)))))
           (is (= -40 (get-promoter-score [1 2 3 4 5 6 7 8 9 10]))))

  (testing "Correctly calculates the NPS when provided custom min/max values"
           (is (= 0 (get-promoter-score [1 2 3 4 5 6 7 8 9 10] 4 7))))

  (testing "Correctly validates provided function parameters"
           (is (thrown? AssertionError (get-promoter-score [-1 0 1])))
           (is (thrown? AssertionError (get-promoter-score [])))
           (is (thrown? AssertionError (get-promoter-score [1 2 3 4 5] 7 3)))))

(deftest get-promoter-summary-test
  (testing "Correctly provides a summary of net promoter score."
           (is (= {:score -40 :mean 5.5 :median 5.5} (get-promoter-summary [1 2 3 4 5 6 7 8 9 10]))))

  (testing "Correctly provides a summary when given custom min/max values"
           (is (= {:score 0 :mean 5.5 :median 5.5} (get-promoter-summary [1 2 3 4 5 6 7 8 9 10] 4 7))))

  (testing "Correctly validates provided function parameters"
           (is (thrown? AssertionError (get-promoter-summary [-1 0 1])))
           (is (thrown? AssertionError (get-promoter-summary [])))
           (is (thrown? AssertionError (get-promoter-summary [1 2 3 4 5] 7 3)))))
