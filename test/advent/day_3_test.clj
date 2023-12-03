(ns advent.day-3-test
  (:require [advent.day-3 :as sut]
            [clojure.test :refer [deftest testing is]]
            [clojure.java.io :as io]))

(defonce sample-input
  (line-seq (io/reader "resources/day_3_sample.txt")))

(deftest lines->part-numbers-test
  (let [parts (set (sut/lines->part-numbers sample-input))]
    (is (= 8 (count parts)))))

(deftest solve-part-one-test
  (is (= 4361 (sut/solve-part-one sample-input))))

(deftest lines->gears-test
  (is (= 2 (count (sut/lines->gears sample-input)))))


(deftest solve-part-two-test
  (is (= 467835 (sut/solve-part-two sample-input))))
