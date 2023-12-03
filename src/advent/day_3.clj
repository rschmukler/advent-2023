(ns advent.day-3
  (:require [clojure.java.io :as io]))

(defn adjacent-coordinates
  "Return a sequence of all adjacent coordinates to `coord`"
  [[x y :as coord]]
  (for [adj-x (range (dec x) (+ 2 x))
        adj-y (range (dec y) (+ 2 y))
        :let  [adj-coord [adj-x adj-y]]
        :when (not= coord adj-coord)]
    adj-coord))

(defn lines->symbols
  "Return the coordinates of symbols in the provided lines"
  [lines]
  (for [[y line] (map-indexed vector lines)
        [x char] (map-indexed vector line)
        :when    (not (re-find #"\d|\." (str char)))]
    [x y]))

(defn lines->part-numbers
  "Return the part numbers in the provided lines"
  [lines]
  (let [symbol-coords (set (lines->symbols lines))]
    (for [[y line] (map-indexed vector lines)
          :let     [number-groups
                    (->> line
                         (map-indexed vector)
                         (partition-by #(some? (re-find #"\d" (str (second %)))))
                         (filter #(re-find #"\d" (str (second (first %))))))]
          group    number-groups
          :let     [coords (for [[x _] group] [x y])
                    has-adjacent-symbol?
                    (->> coords
                         (mapcat adjacent-coordinates)
                         (filter symbol-coords)
                         seq)]
          :when    has-adjacent-symbol?]
      {:coordinates coords
       :number      (Integer/parseInt (apply str (map second group)))})))

(defn solve-part-one
  "Return the solution to part one"
  [lines]
  (->> lines
       (lines->part-numbers)
       (map :number)
       (apply +)))

(defn lines->gears
  "Return the gears in the provided `lines`"
  [lines]
  (let [parts        (lines->part-numbers lines)
        coord->parts (->> (for [part  parts
                                coord (:coordinates part)]
                            [coord part])
                          (into {}))]
    (for [[y line] (map-indexed vector lines)
          [x char] (map-indexed vector line)
          :when    (re-find #"\*" (str char))
          :let
          [coord [x y]
           adjacent-parts (->> coord
                               (adjacent-coordinates)
                               (keep coord->parts)
                               (set))]
          :when    (= 2 (count adjacent-parts))]
      {:coordinate     coord
       :ratio          (apply * (map :number adjacent-parts))
       :adjacent-parts adjacent-parts})))

(defn solve-part-two
  "Return the solution to part two"
  [lines]
  (->> lines
       (lines->gears)
       (map :ratio)
       (apply +)))

(defonce puzzle-input
  (line-seq (io/reader "resources/day_3.txt")))

(comment
  (solve-part-one puzzle-input)
  (solve-part-two puzzle-input))
