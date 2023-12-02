(ns advent.day-2-test
  (:require [advent.day-2 :as sut]
            [clojure.test :refer [deftest testing is]]
            [clojure.java.io :as io]
            [clojure.string :as str]))

(defonce sample-lines
  (->>
    (io/reader "resources/day_2_sample.txt")
    (line-seq)))

(deftest line->game-test
  (is (= {:id 1
          :rounds [{:blue 3
                    :red  4}
                   {:red   1
                    :green 2
                    :blue  6}
                   {:green 2}]}
         (sut/line->game
           "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green"))))

(deftest game->max-quantities
  (is (= {:blue  6
          :red   4
          :green 2}
         (sut/game->max-quantities
           {:id     1
            :rounds [{:blue 3
                      :red  4}
                     {:red   1
                      :green 2
                      :blue  6}
                     {:green 2}]}))))

(deftest possible?-test
  (is (= #{1 2 5}
         (->> sample-lines
              (map sut/line->game)
              (filter (partial sut/possible? {:red   12
                                              :green 13
                                              :blue  14}))
              (map :id)
              set))))

(deftest solve-part-one-test
  (is (= 8
         (sut/solve-part-one
           {:red   12
            :green 13
            :blue  14}
           sample-lines))))

(deftest game->power-test
  (is (= 48
         (sut/game->power
           {:id     1
            :rounds [{:blue 3
                      :red  4}
                     {:red   1
                      :green 2
                      :blue  6}
                     {:green 2}]}))))

(deftest solve-part-two-test
  (is (= 2286
         (sut/solve-part-two sample-lines))))
