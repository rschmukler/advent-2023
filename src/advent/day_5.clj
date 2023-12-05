(ns advent.day-5
  (:require [clojure.java.io :as io]))


(def puzzle-input
  (line-seq (io/reader "resources/day_5.txt")))

(defn optimal-val
  [[start end] [paths & rest-traversals]]
  (if-not paths
    start
    (loop [position                                   start
           [{path-start :start
             path-end   :end
             delta      :delta
             :as        path} & rest-paths :as paths] paths
           optimal                                    ##Inf]
      (cond
        (> position end)
        optimal
        (nil? path)
        (min optimal (optimal-val [position end] rest-traversals))
        (< position path-start)
        (recur
          path-start
          paths
          (min optimal (optimal-val [position (min (dec path-start) end)] rest-traversals)))
        (<= path-start position path-end)
        (let [intersection-end (min path-end end)]
          (recur
            (inc intersection-end)
            rest-paths
            (min
              optimal
              (optimal-val [(+ delta position) (+ delta intersection-end)] rest-traversals))))
        (>= position path-start)
        (recur position rest-paths optimal)
        :else
        optimal))))

(defn parse-input
  [input]
  (let [line->ints #(map parse-long (re-seq #"\d+" %))
        [seeds
         & traversals]
        (->> input
             (partition-by #(= "" %))
             (remove #{'("")}))
        traversals
        (for [t    traversals
              :let [paths (rest t)]]
          (->> paths
               (map line->ints)
               (map (fn [[dest src src-range]]
                      {:start src :end (dec (+ src-range src)) :delta (- dest src)}))
               (sort-by :start)))
        seeds      (line->ints (first seeds))]
    [seeds traversals]))

(defn solve
  [seed-ranges traversals]
  (->> seed-ranges
       (map #(optimal-val % traversals))
       sort
       first))

(defn solve-part-one
  [input]
  (let [[seeds traversals] (parse-input input)
        seed-ranges        (->> seeds
                                (map #(vector % (inc %))))]
    (solve seed-ranges traversals)))

(defn solve-part-two
  [input]
  (let [[seeds traversals] (parse-input input)
        seed-ranges        (->> seeds
                                (partition 2)
                                (map #(vector (first %) (apply + %))))]
    (solve seed-ranges traversals)))

(comment
  (solve-part-one puzzle-input)
  (solve-part-two puzzle-input))
