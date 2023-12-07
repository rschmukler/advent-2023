(ns advent.day-7-test
  (:require [advent.day-7 :as sut]
            [clojure.test :refer [deftest testing is are]]
            [clojure.java.io :as io]))

(defonce sample-input
  (line-seq (io/reader "resources/day_7_sample.txt")))

(deftest parse-hand-test
  (are [hand input] (= hand (sut/parse-hand input))
    [:five-of-a-kind "AAAAA"] "AAAAA"
    [:four-of-a-kind "AAAAT"] "AAAAT"
    [:two-pair "T6T6A"]       "T6T6A")

  (testing "jokers wild"
    (are [hand input] (= hand (sut/parse-hand true input))
      [:five-of-a-kind "AJAJA"] "AJAJA")))

(deftest solve-part-one-test
  (is (= 6440 (sut/solve sample-input)))
  (is (= 5905 (sut/solve sample-input :jokers-wild? true))))
