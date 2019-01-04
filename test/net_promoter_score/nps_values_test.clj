(ns net-promoter-score.nps-values-test
  (:require [clojure.test :refer :all]
            [net-promoter-score.nps-values :refer :all]))

(deftest score-mean-test
  (testing "Correctly calculates the mean value of a list of values"
           (is (= 5 (int (nps-mean [5 5 5 5]))))
           (is (= 2.5 (nps-mean [10 0 0 0]))))

  (testing "Correctly validates the inputs provided to the function"
           (is (thrown? AssertionError (nps-mean [-1 1 2 3 4]))))

  (testing "Errors if given zero values to work with"
           (is (thrown? AssertionError (nps-mean [])))))

(deftest find-middle-vector-elements-test
  (testing "Correctly finds the middle element on a list with one element"
           (is (= '(0) (find-middle-vector-elements 1))))
  (testing "Correctly finds the middle elements on a list with two elements"
           (is (= '(0 1) (find-middle-vector-elements 2))))
  (testing "Correctly finds the middle elements on a list with more than two elements"
           (is (= 1 (first (find-middle-vector-elements 3))))
           (is (= '(2 1) (find-middle-vector-elements 4)))))

(deftest score-median-test
  (testing "Correctly calculates the median value of a list of values with an even count"
           (is (= 2.5 (nps-median [1 2 3 4])))
           (is (= 5 (int (nps-median [0 10]))))
           (is (= 8 (int (nps-median [1 7 9 10]))))))

