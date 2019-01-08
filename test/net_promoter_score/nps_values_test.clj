(ns net-promoter-score.nps-values-test
  (:require [clojure.test :refer :all]
            [net-promoter-score.nps-values :as values]))

(defn ^:private double-values-are-approximately-equal?
  "Compares double values for 'close enough' equality checks when checking variance/standard deviation results."
  [first-val second-val]
  (<= -0.00001 (- first-val second-val) 0.00001))

(deftest score-mean-test
  (testing "Correctly calculates the mean value of a list of values"
           (is (= 5 (int (values/nps-mean [5 5 5 5]))))
           (is (= 2.5 (values/nps-mean [10 0 0 0]))))

  (testing "Correctly validates the inputs provided to the function"
           (is (thrown? AssertionError (values/nps-mean [-1 1 2 3 4]))))

  (testing "Returns N/A when given no values to work with"
           (is (= "N/A" (values/nps-mean [])))))

(deftest find-middle-vector-elements-test
  (testing "Correctly finds the middle element on a list with one element"
           (is (= '(0) (values/find-middle-vector-elements 1))))
  (testing "Correctly finds the middle elements on a list with two elements"
           (is (= '(0 1) (values/find-middle-vector-elements 2))))
  (testing "Correctly finds the middle elements on a list with more than two elements"
           (is (= 1 (first (values/find-middle-vector-elements 3))))
           (is (= '(2 1) (values/find-middle-vector-elements 4)))))

(deftest score-median-test
  (testing "Correctly calculates the median value of a list of values with an even count"
           (is (= 2.5 (values/nps-median [1 2 3 4])))
           (is (= 5 (int (values/nps-median [0 10]))))
           (is (= 8 (int (values/nps-median [1 7 9 10])))))

  (testing "Correctly validates the provided parameters to the function"
           (is (thrown? AssertionError (values/nps-median [10 11 12]))))

  (testing "Correctly returns N/A when provided an empty set"
           (is (= "N/A" (values/nps-median [])))))

(deftest nps-variance-test
  (testing "Correctly calculates simple variance when there is none"
           (is (= 0.0 (values/nps-variance [2 2 2]))))

  (testing "Correctly calculates variance when some exists"
           (is (= 0.25 (values/nps-variance [1 2]))))

  (testing "Correctly calculates complicated variance values"
           (is
             (= true
                (double-values-are-approximately-equal? (double 40/9) (values/nps-variance [4 7 3 4 5 7 8 6 10])))))

  (testing "Correctly validates the parameters passed to the function"
           (is (thrown? AssertionError (values/nps-variance [10 0 11]))))

  (testing "Correctly returns not applicable when provided an empty parameter set"
           (is (= "N/A" (values/nps-variance [])))))

(deftest nps-standard-deviation-test
  (testing "Correctly calculates simple standard deviation when there is no variance"
           (is (= 0.0 (values/nps-standard-deviation [2 2 2]))))

  (testing "Correctly calculates simple standard deviation when minor variance exists"
           (is (= 0.5 (values/nps-standard-deviation [1 2]))))
  (testing "Correctly calculates complicated standard deviation when significant variance exists"
           (is
             (= true
                (double-values-are-approximately-equal? 2.1081851067789 (values/nps-standard-deviation [4 7 3 4 5 7 8 6 10]))))))



