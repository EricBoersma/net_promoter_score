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

(deftest in-nps-range-test
  (testing "Correctly returns true when all values are valid"
           (is (= true (in-nps-range [1 2 3 4 5 6 7 8 9 10]))))

  (testing "Correctly returns false when some values are invalid"
           (is (= false (in-nps-range [-1 1 2 3 4]))))

  (testing "Correctly returns true when an empty vector is provided"
           (is (= true (in-nps-range [])))))

(deftest find-all-detractors-test
  (testing "Correctly finds all the scores classified as detractors by default NPS prioritization"
           (is (= '(1 2 3 4 5 6) (find-all-detractors [1 2 3 4 5 6 7 8 9 10]))))

  (testing "Correctly finds all the scores classified as detractors by custom NPS prioritization"
           (is (= '(1 2 3) (find-all-detractors [1 2 3 4 5 6 7 8 9 10] 3))))

  (testing "Correctly returns an empty list when there are no matching items"
           (is (= '() (find-all-detractors [10 10 10]))))

  (testing "Correctly validates the provided values in the vector for validity to NPS scores"
           (is (thrown? AssertionError (find-all-detractors [-1 0 1])))))

(deftest find-all-promorters-test
  (testing "Correctly finds all the scores classified as promoters by default NPS prioritization"
           (is (= '(9 10) (find-all-promoters [1 2 3 4 5 6 7 8 9 10]))))

  (testing "Correctly finds all the scores classified as promoters by custom NPS prioritization"
           (is (= '(5 6 7) (find-all-promoters [1 2 3 4 5 6 7] 5))))

  (testing "Correctly returns an empty list when there are no matching items"
           (is (= '() (find-all-promoters [1 2 3 4]))))

  (testing "Correctly validates the provided values in the vector for validity to NPS scores"
           (is (thrown? AssertionError (find-all-promoters [-1 0 1])))))

(deftest find-all-passives-test
  (testing "Correctly finds all the scores classified as passives by default NPS prioritization"
           (is (= '(7 8) (find-all-passives [1 2 3 4 5 6 7 8 9 10]))))

  (testing "Correctly finds all the scores classified as passives by custom NPS prioritization"
           (is (= '(4 5) (find-all-passives [1 2 3 4 5 6 7] 3 6))))

  (testing "Correctly returns an empty list when there are no matching items"
           (is (= '() (find-all-passives [1 10]))))

  (testing "Correctly validates the provided values in the vector for validity to NPS scores"
           (is (thrown? AssertionError (find-all-passives [-1 0 1]))))
  )