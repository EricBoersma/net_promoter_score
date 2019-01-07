(ns net-promoter-score.nps-validation-test
  (:require [clojure.test :refer :all]
            [net-promoter-score.nps-validation :as validate]))

(deftest in-nps-range-test
  (testing "Correctly returns true when all values are valid"
           (is (= true (validate/in-nps-range? [1 2 3 4 5 6 7 8 9 10]))))

  (testing "Correctly returns false when some values are invalid"
           (is (= false (validate/in-nps-range? [-1 1 2 3 4]))))

  (testing "Correctly returns true when an empty vector is provided"
           (is (= true (validate/in-nps-range? [])))))