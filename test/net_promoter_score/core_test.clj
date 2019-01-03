(ns net-promoter-score.core-test
  (:require [clojure.test :refer :all]
            [net-promoter-score.core :refer :all]))

(defn get-random-detractor-values
  "Gets a list of the specified number of detractor values (less than 7)"
  [count]
  (take count (repeatedly #(rand-int 7))))

(defn get-random-passive-values
  "Gets a list of the specified number of passive values (7 and 8)"
  [count]
  (take count (repeatedly #(+ (rand-int 2) 7))))

(defn get-random-promoter-values
  "Gets a list of the specified number of promoter values (9 and 10)"
  [count]
  (take count (repeatedly #(+ (rand-int 2) 9))))

(deftest get-promoter-score-test
  (testing "Correctly calculates the NPS based on some large number of values"
           (is (= 0 (get-promoter-score (flatten (conj (get-random-detractor-values 10) (get-random-passive-values 10) (get-random-promoter-values 10))))))))