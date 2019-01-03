(ns net-promoter-score.nps-calc-test
  (:require [clojure.test :refer :all]
            [net-promoter-score.nps-calc :refer :all]))

(deftest calculate-percentile-value-test
  (testing "Correctly calculates simple fractions"
           (is (= 20 (calculate-percentile-value 1 5)))
           (is (= 0 (calculate-percentile-value 0 1)))
           (is (= 100 (calculate-percentile-value 10 10)))
           (is (= 50 (calculate-percentile-value 10 20))))

  (testing "Correctly calculates complex fractions"
           (is (= 33 (calculate-percentile-value 1 3)))
           (is (= 66 (calculate-percentile-value 2 3))))

  (testing "Correctly errors when given bad input"
           (is (thrown? AssertionError (calculate-percentile-value -1 3)))
           (is (thrown? AssertionError (calculate-percentile-value 2 -3)))
           (is (thrown? AssertionError (calculate-percentile-value 2 0)))))

(deftest calculate-net-promoter-score-test
  (testing "Correctly calculates with simple values"
           (is (= 40 (calculate-net-promoter-score 1 1 3)))
           (is (= 60 (calculate-net-promoter-score 1 0 4)))
           (is (= 0 (calculate-net-promoter-score 0 1 0)))
           (is (= 100 (calculate-net-promoter-score 0 0 100))))

  (testing "Correctly calculates negative scores"
           (is (= -40 (calculate-net-promoter-score 3 1 1)))
           (is (= -60 (calculate-net-promoter-score 4 0 1)))
           (is (= -100 (calculate-net-promoter-score 10 0 0))))

  (testing "Correctly validates function inputs"
           (is (thrown? AssertionError (calculate-net-promoter-score -1 10 10)))
           (is (thrown? AssertionError (calculate-net-promoter-score 10 -1 10)))
           (is (thrown? AssertionError (calculate-net-promoter-score 10 10 -1)))
           (is (thrown? AssertionError (calculate-net-promoter-score 0 0 0)))))