(ns advent.day-10
  (:require [clojure.java.io :as io]
            [clojure.math :as math]))

(defonce puzzle-input
  (line-seq (io/reader "resources/day_10.txt")))

(defn find-start
  [m]
  (->> m
       (filter (comp :start? val))
       (first)
       (key)))

(defn infer-starting-piece
  "Return `m` with the starting pieces connections inferred"
  [m]
  (let [[x y :as start-pos] (find-start m)
        north?              (some? (:south? (get m [x (dec y)])))
        south?              (some? (:north? (get m [x (inc y)])))
        east?               (some? (:west? (get m [(inc x) y])))
        west?               (some? (:east? (get m [(dec x) y])))]
    (assoc
      m
      start-pos
      {:pos    start-pos
       :north? north?
       :south? south?
       :east?  east?
       :west?  west?
       :start? true
       :char   (case [north? south? east?]
                 [true true false]  \|
                 [false false true] \-
                 [true false true]  \L
                 [true false false] \J
                 [false true false] \7
                 [false true true]  \F)})))

(defn input->map
  [lines]
  (->> (for [[y line] (map-indexed vector lines)
             [x c]    (map-indexed vector line)
             :when    (not= \. c)]
         [[x y]
          {:north?  (#{\| \L \J} c)
           :south?  (#{\| \7 \F} c)
           :east?   (#{\- \L \F} c)
           :west?   (#{\- \J \7} c)
           :start?  (#{\S} c)
           :corner? (#{\L \J \F \7} c)
           :char    c
           :pos     [x y]}])
       (into {})
       (infer-starting-piece)))

(defn valid-neighbors
  [m [x y :as pos]]
  (let [{:keys [north? east? south? west?]} (get m pos)]
    (->> [(when north?
            [x (dec y)])
          (when east?
            [(inc x) y])
          (when south?
            [x (inc y)])
          (when west?
            [(dec x) y])]
         (filter m))))

(defn find-loop
  [m]
  (loop [position (find-start m)
         path     [position]
         seen     #{position}]
    (let [[neighbor & _] (remove
                           seen
                           (valid-neighbors m position))]
      (if-not neighbor
        path
        (recur
          neighbor
          (conj path neighbor)
          (conj seen neighbor))))))

(defn solve-part-one
  [input]
  (-> input
      (input->map)
      (find-loop)
      (count)
      (/ 2)
      (math/ceil)
      int))

(defn find-bounding-box
  [m]
  (let [puzzle-loop (find-loop m)
        xs          (map first puzzle-loop)
        ys          (map second puzzle-loop)]
    {:max-x (apply max xs)
     :min-x (apply min xs)
     :max-y (apply max ys)
     :min-y (apply min ys)}))

(defn squeeze-output
  [m [x y]]
  (for [neighbor [[x (inc y)]
                  [x (dec y)]
                  [(dec x) y]
                  [(inc x) y]]
        :when    (:corner? (m neighbor))]
    (let [the-loop   (find-loop m)
          loop-count (count the-loop)]
      (->> the-loop
           (cycle)
           (take loop-count)
           (drop-while #(not= neighbor %))
           (map m)
           (keep
             (fn [{:keys [corner?
                          east? west? north? south?]
                   [x y] :pos}]
               (when corner?
                 (cond
                   (and (not east?) (not (m [(inc x) y])))
                   [(inc x) y]
                   (and (not west?) (not (m [(dec x) y])))
                   [(dec x) y]
                   (and (not north?) (not (m [x (inc y)])))
                   [x (inc y)]
                   (and (not south?) (not (m [x (dec y)])))
                   [x (dec y)]
                   :else
                   nil))))))))

(defn enclosed-tiles
  [m]
  (let [loop-tiles      (find-loop m)
        remove?         (set (concat loop-tiles (keys m)))
        {:keys [min-x
                min-y
                max-x
                max-y]} (find-bounding-box m)
        out-of-bounds?  (fn out-of-bounds?
                          [[x y]]
                          (not
                            (and (<= min-x x max-x)
                                 (<= min-y y max-y))))
        all-tiles       (for [x     (range min-x (inc max-x))
                              y     (range min-y (inc max-y))
                              :let  [pos [x y]]
                              :when (not (remove? pos))]
                          pos)]
    (loop [[start & rest-starts :as starts]    all-tiles
           [[x y :as seg-pos] & rest-to-check] [start]
           seen-segment                        #{start}
           segments                            #{}]
      (cond
        (nil? start)             segments
        (nil? seg-pos)           (let [rest-starts (remove seen-segment rest-starts)]
                                   (recur rest-starts
                                          [(first rest-starts)]
                                          #{(first rest-starts)}
                                          (conj segments seen-segment)))
        (out-of-bounds? seg-pos) (let [rest-starts (remove seen-segment rest-starts)]
                                   (recur rest-starts
                                          [(first rest-starts)]
                                          #{(first rest-starts)}
                                          segments))
        :else
        (let [new-neighbors
              (for [new-x (range (dec x) (+ 2 x))
                    new-y (range (dec y) (+ 2 y))
                    :let  [neighbor [new-x new-y]]
                    :when (and (not (seen-segment neighbor))
                               (not (remove? neighbor)))]
                neighbor)]
          (recur
            starts
            (into rest-to-check new-neighbors)
            (into seen-segment new-neighbors)
            segments))))))


(defn solve-part-two
  [input]
  (->> input
       (input->map)
       (enclosed-tiles)
       (apply concat)
       (count)))

(comment
  (-> puzzle-input
      (input->map)
      (enclosed-tiles))
  (solve-part-one puzzle-input)
  (time
    (solve-part-two puzzle-input))
  (count (apply concat (enclosed-tiles puzzle-input))))
