(ns advent.day-4-test
  (:require [advent.day-4 :as sut]
            [clojure.test :refer [deftest testing is]]
            [clojure.java.io :as io]))

(defonce sample-input
  (line-seq (io/reader "resources/day_4_sample.txt")))

(deftest line->card-test
  (is (= {:id 1
          :winning-numbers #{41 48 83 86 17}
          :player-numbers [83 86 6 31 17 9 48 53]}
         (sut/line->card
           "Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53"))))

(deftest card->point-value-test
  (is (= 8
         (sut/card->point-value
           {:id              1
            :winning-numbers #{41 48 83 86 17}
            :player-numbers  [83 86 6 31 17 9 48 53]}))))

(deftest solve-part-one-test
  (is (= 13 (sut/solve-part-one sample-input))))

(deftest card->coppies
  (let [cards (map sut/line->card sample-input)]
    (is (= #{2 3 4 5}
           (set (sut/card->coppies (first cards)))))))

(deftest solve-part-two-test
  (is (= 30 (sut/solve-part-two sample-input))))
