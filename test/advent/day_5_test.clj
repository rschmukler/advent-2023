(ns advent.day-5-test
  (:require [advent.day-5 :as sut]
            [clojure.test :refer [deftest testing is]]
            [clojure.java.io :as io]))

(defonce sample-input
  (line-seq (io/reader "resources/day_5_sample.txt")))

(def sample-garden-map
  (sut/input->garden-map sample-input))

(deftest solve-part-one-test
  (is (= 35 (sut/solve-part-one sample-input))))

(deftest solve-part-two-test
  (is (= 46 (sut/solve-part-two sample-input))))
