(ns advent.day-1
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn part-one-line->digits
  "Take a line of text and return the first and last digit"
  [line]
  (let [digit-strs (re-seq #"\d" line)]
    [(Integer/parseInt (first digit-strs))
     (Integer/parseInt (last digit-strs))]))

(defn solve-with
  "Take the provided lines and return the sum of digits for part one"
  [f lines]
  (reduce
    +
    (for [line lines
          :let [[tens ones] (f line)]]
      (+ (* 10 tens) ones))))

;; Begin part two related functionality
(def text->digit
  "A map from text literals to corresponding digits"
  {"one"   1 "1" 1
   "two"   2 "2" 2
   "three" 3 "3" 3
   "four"  4 "4" 4
   "five"  5 "5" 5
   "six"   6 "6" 6
   "seven" 7 "7" 7
   "eight" 8 "8" 8
   "nine"  9 "9" 9})

(def text-literal-regex
  "Regex used to match on the text literals in the text->digit map"
  (->> text->digit
       keys
       (clojure.string/join "|")
       (format "(?=(%s))")
       re-pattern))

(defn part-two-line->digits
  "Take a line of text and return the first and last digit using the rules in part two"
  [line]
  (->> line
       (re-seq text-literal-regex)
       ((juxt first last))
       (mapv (comp text->digit second))))

(comment
  (->> (line-seq (io/reader "resources/day_1.txt"))
       (solve-with part-one-line->digits))
  (->> (line-seq (io/reader "resources/day_1.txt"))
       (solve-with part-two-line->digits))
  )
