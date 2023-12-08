(ns advent.day-8
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def puzzle-input
  (line-seq (io/reader "resources/day_8.txt")))


(defn line->node
  [line]
  (let [[id left right] (re-seq #"[\w]{3}" line)]
    {:id    id
     \L  left
     \R right}))

(defn nodes->map
  [nodes]
  (into
    {}
    (map (juxt :id identity))
    nodes))

(defn parse-input
  [lines]
  (let [[instructions _ & node-lines] lines]
    [instructions
     (->> node-lines
          (map line->node)
          (nodes->map))]))

(defn navigate
  [node-map instructions start end]
  (->> (cycle instructions)
       (reductions
         (fn [current-node instruction]
           (get node-map (current-node instruction)))
         (get node-map start))
       (take-while (complement #(end (:id %))))))

(defn solve-part-one
  [input]
  (let [[instructions node-map] (parse-input input)]
    (count (navigate node-map instructions "AAA" #(= % "ZZZ")))))

;; Part 2 related functions

(defn find-starting-nodes
  [node-map]
  (->> node-map
       (vals)
       (filter #(str/ends-with? (:id %) "A"))))

(defn gcd
  [nums]
  (let [max-num (apply min nums)]
    (loop [x 2]
      (cond
        (> x max-num)                    1
        (every? #(zero? (rem % x)) nums) x
        :else                            (recur (inc x))))))

(defn solve-part-two
  [input]
  (let [[instructions node-map] (parse-input input)
        starting-nodes          (find-starting-nodes node-map)
        answers
        (->> starting-nodes
             (map
               (fn [starting-node]
                 (->> (navigate node-map
                                instructions
                                (:id starting-node)
                                #(str/ends-with? % "Z"))
                      (count)))))
        div                     (gcd answers)]
    (->> answers
         (map #(/ % div))
         (apply * div))))


(comment
  (solve-part-one puzzle-input)
  (solve-part-two puzzle-input)
  )
