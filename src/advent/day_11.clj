(ns advent.day-11
  (:require [clojure.java.io :as io]))

(defonce puzzle-input
  (line-seq (io/reader "resources/day_11.txt")))

(defn lines->unexpanded-map
  [lines]
  (->> (for [[y line] (map-indexed vector lines)
             [x c]    (map-indexed vector line)
             :when    (= \# c)]
         [x y])
       (map-indexed vector)
       (map (juxt second #(array-map :id (inc (first %)) :coords (second %))))
       (into {})))

(defn find-empty-space
  [galaxy-map]
  (let [coords  (keys galaxy-map)
        seen-xs (set (map first coords))
        seen-ys (set (map second coords))]
    {:columns (->> (range (apply min seen-xs)
                          (inc (apply max seen-xs)))
                   (remove seen-xs)
                   (vec))
     :rows    (->> (range (apply min seen-ys)
                          (inc (apply max seen-ys)))
                   (remove seen-ys)
                   (vec))}))

(defn expand-map
  "Return the galaxy with expansions applied"
  ([galaxy-map] (expand-map 2 galaxy-map))
  ([distance-multiple galaxy-map]
   (let [{:keys [rows columns]} (find-empty-space galaxy-map)]
     (->> (for [[[x y] galaxy] galaxy-map
                :let
                [new-x (->> columns
                            (filter #(< % x))
                            (count)
                            (* (dec distance-multiple))
                            (+ x))
                 new-y (->> rows
                            (filter #(< % y))
                            (count)
                            (* (dec distance-multiple))
                            (+ y))]]
            [[new-x new-y] (assoc galaxy :coords [new-x new-y])])
          (into {})))))

(defn galaxy-pairs
  [galaxy-map]
  (into
    #{}
    (for [first-id  (range 1 (inc (count galaxy-map)))
          second-id (range 1 (inc (count galaxy-map)))
          :when     (not= first-id second-id)]
      #{first-id second-id})))

(defn find-distance
  [id->galaxy from-id to-id]
  (let [[from-x from-y] (:coords (id->galaxy from-id))
        [to-x to-y]     (:coords (id->galaxy to-id))]
    (+ (abs (- from-x to-x))
       (abs (- to-y from-y)))))

(defn solve
  [distance-multiple input]
  (let [galaxies   (->> input
                        (lines->unexpanded-map)
                        (expand-map distance-multiple))
        id->galaxy (->> (vals galaxies)
                        (map (juxt :id identity))
                        (into {}))]
    (->> galaxies
         (galaxy-pairs)
         (map #(find-distance id->galaxy (first %) (second %)))
         (apply +))))

(comment
  (solve 2 puzzle-input)
  (solve 1000000 puzzle-input)
  )
