(ns advent.day-6-test
  (:require  [clojure.test :refer [deftest testing is]]
             [advent.day-6 :as sut]))

(def example-input
  "Time:      7  15   30
   Distance:  9  40  200")

(deftest parse-input-test
  (is (= {7 9
          15 40
          30 200}
         (sut/parse-input example-input))))

(deftest compute-distance-traveled-test
  (is (= 6 (sut/compute-distance-traveled 7 1)))
  (is (= 10 (sut/compute-distance-traveled 7 2))))

(deftest ways-to-win-test
  (is (= 4 (count (sut/ways-to-win [7 9])))))


(deftest solve-part-one-test
  (is (= 288 (sut/solve-part-one example-input))))

(deftest parse-input-part-two-test
  (is (= [71530 940200]
         (sut/parse-input-part-two example-input))))

(deftest find-optimal-hold-time
  (is (#{3 4} (sut/find-optimal-hold-time [7 5])))
  (is (= 15 (sut/find-optimal-hold-time [30 200]))))

(deftest find-winning-bound-test
  (is (= 1 (sut/find-winning-bound :left 3 [7 5])))
  (is (= 6 (sut/find-winning-bound :right 3 [7 5]))))

(deftest find-winning-range-test
  (is (= [1 6] (sut/find-winning-range [7 5])))
  (is (= [4 11] (sut/find-winning-range [15 40])))
  (is (= [11 19] (sut/find-winning-range [30 200]))))

(deftest solve-part-two-test
  (is (= 71503 (sut/solve-part-two example-input))))
