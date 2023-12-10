(ns advent.day-9-test
  (:require [advent.day-9 :as sut]
            [clojure.test :refer [deftest testing is]]
            [clojure.string :as str]))

(def sample-input
  (str/split-lines
  "0 3 6 9 12 15
   1 3 6 10 15 21
   10 13 16 21 30 45"))

(deftest -history-test
  (is (= [3]
         (sut/-history [0 3]))))

(deftest next-value-test
  (is (= 18
         (sut/next-value [0 3 6 9 12 15])))
  (is (= 28
         (sut/next-value [1 3 6 10 15 21])))
  (is (= 68
         (sut/next-value [10 13 16 21 30 45]))))

(deftest solve-part-one-test
  (is (= 114 (sut/solve-part-one sample-input))))

(deftest solve-part-two-test
  (is (= 2 (sut/solve-part-two sample-input))))
