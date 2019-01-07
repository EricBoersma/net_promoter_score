(ns net-promoter-score.nps-calc-test
  (:require [clojure.test :refer :all]
            [net-promoter-score.nps-calc :as nps-calc]))

(deftest calculate-percentile-value-test
  (testing "Correctly calculates simple fractions"
           (is (= 20 (nps-calc/calculate-percentile-value 1 5)))
           (is (= 0 (nps-calc/calculate-percentile-value 0 1)))
           (is (= 100 (nps-calc/calculate-percentile-value 10 10)))
           (is (= 50 (nps-calc/calculate-percentile-value 10 20))))

  (testing "Correctly calculates complex fractions"
           (is (= 33 (nps-calc/calculate-percentile-value 1 3)))
           (is (= 66 (nps-calc/calculate-percentile-value 2 3))))

  (testing "Correctly errors when given bad input"
           (is (thrown? AssertionError (nps-calc/calculate-percentile-value -1 3)))
           (is (thrown? AssertionError (nps-calc/calculate-percentile-value 2 -3)))
           (is (thrown? AssertionError (nps-calc/calculate-percentile-value 2 0)))))

(deftest calculate-net-promoter-score-test
  (testing "Correctly calculates with simple values"
           (is (= 40 (nps-calc/calculate-net-promoter-score 1 1 3)))
           (is (= 60 (nps-calc/calculate-net-promoter-score 1 0 4)))
           (is (= 0 (nps-calc/calculate-net-promoter-score 0 1 0)))
           (is (= 100 (nps-calc/calculate-net-promoter-score 0 0 100))))

  (testing "Correctly calculates negative scores"
           (is (= -40 (nps-calc/calculate-net-promoter-score 3 1 1)))
           (is (= -60 (nps-calc/calculate-net-promoter-score 4 0 1)))
           (is (= -100 (nps-calc/calculate-net-promoter-score 10 0 0))))

  (testing "Correctly validates function inputs"
           (is (thrown? AssertionError (nps-calc/calculate-net-promoter-score -1 10 10)))
           (is (thrown? AssertionError (nps-calc/calculate-net-promoter-score 10 -1 10)))
           (is (thrown? AssertionError (nps-calc/calculate-net-promoter-score 10 10 -1)))
           (is (thrown? AssertionError (nps-calc/calculate-net-promoter-score 0 0 0)))))

(deftest find-all-detractors-test
  (testing "Correctly finds all the scores classified as detractors by default NPS prioritization"
           (is (= '(1 2 3 4 5 6) (nps-calc/find-all-detractors [1 2 3 4 5 6 7 8 9 10]))))

  (testing "Correctly finds all the scores classified as detractors by custom NPS prioritization"
           (is (= '(1 2 3) (nps-calc/find-all-detractors [1 2 3 4 5 6 7 8 9 10] 3))))

  (testing "Correctly returns an empty list when there are no matching items"
           (is (= '() (nps-calc/find-all-detractors [10 10 10]))))

  (testing "Correctly validates the provided values in the vector for validity to NPS scores"
           (is (thrown? AssertionError (nps-calc/find-all-detractors [-1 0 1])))))

(deftest find-all-promorters-test
  (testing "Correctly finds all the scores classified as promoters by default NPS prioritization"
           (is (= '(9 10) (nps-calc/find-all-promoters [1 2 3 4 5 6 7 8 9 10]))))

  (testing "Correctly finds all the scores classified as promoters by custom NPS prioritization"
           (is (= '(5 6 7) (nps-calc/find-all-promoters [1 2 3 4 5 6 7] 5))))

  (testing "Correctly returns an empty list when there are no matching items"
           (is (= '() (nps-calc/find-all-promoters [1 2 3 4]))))

  (testing "Correctly validates the provided values in the vector for validity to NPS scores"
           (is (thrown? AssertionError (nps-calc/find-all-promoters [-1 0 1])))))

(deftest find-all-passives-test
  (testing "Correctly finds all the scores classified as passives by default NPS prioritization"
           (is (= '(7 8) (nps-calc/find-all-passives [1 2 3 4 5 6 7 8 9 10]))))

  (testing "Correctly finds all the scores classified as passives by custom NPS prioritization"
           (is (= '(4 5) (nps-calc/find-all-passives [1 2 3 4 5 6 7] 3 6))))

  (testing "Correctly returns an empty list when there are no matching items"
           (is (= '() (nps-calc/find-all-passives [1 10]))))

  (testing "Correctly validates the provided values in the vector for validity to NPS scores"
           (is (thrown? AssertionError (nps-calc/find-all-passives [-1 0 1]))))
  )