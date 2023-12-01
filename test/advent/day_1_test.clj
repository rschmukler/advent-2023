(ns advent.day-1-test
  (:require [advent.day-1 :as sut]
            [clojure.test :refer [deftest is are]]))

(deftest part-one-line->digits-test
  (are [line result] (= result (sut/part-one-line->digits line))
    "1abc2"       [1 2]
    "pqr3stu8vwx" [3 8]
    "a1b2c3d4e5f" [1 5]
    "treb7uchet"  [7 7]))

(deftest part-two-line->digits-test
  (are [line result] (= result (sut/part-two-line->digits line))
    "two1nine" [2 9]
    "eightwothree" [8 3]
    "abcone2threexyz" [1 3]
    "xtwone3four" [2 4]
    "4nineeightseven2" [4 2]
    "zoneight234" [1 4]
    "7pqrstsixteen" [7 6]))

(deftest solve-part-one-test
  (is (= 142
         (sut/solve-with
           sut/part-one-line->digits
           ["1abc2"
            "pqr3stu8vwx"
            "a1b2c3d4e5f"
            "treb7uchet"])))
  (is (= 281
         (sut/solve-with
           sut/part-two-line->digits
           ["two1nine"
            "eightwothree"
            "abcone2threexyz"
            "xtwone3four"
            "4nineeightseven2"
            "zoneight234"
            "7pqrstsixteen"]))))
