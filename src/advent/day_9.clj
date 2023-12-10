(ns advent.day-9
  (:require [clojure.java.io :as io]))

(def puzzle-input
  (line-seq (io/reader "resources/day_9.txt")))

(defn -history
  [line]
  (->> line
       (partition 2 1)
       (map
         (comp
           #(apply - %)
           reverse))))

(defn next-value
  [line]
  (let [history (-history line)]
    (if (every? zero? history)
      (last line)
      (+ (last line) (next-value history)))))

(defn solve-part-one
  [input]
  (->> input
       (map #(map parse-long (re-seq #"-?\d+" %)))
       (map next-value)
       (apply +)))

(defn solve-part-two
  [input]
  (->> input
       (map #(reverse (map parse-long (re-seq #"-?\d+" %))))
       (map next-value)
       (apply +)))


(comment
  (solve-part-one puzzle-input)
  (solve-part-two puzzle-input))
