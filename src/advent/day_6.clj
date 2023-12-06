(ns advent.day-6
  (:require [clojure.string :as str]
            [clojure.math :as math]))

(def puzzle-input
  "Time:        53     71     78     80
   Distance:   275   1181   1215   1524")

(defn parse-input
  [input]
  (let [[times distances] (str/split-lines input)
        ->longs #(map parse-long (re-seq #"\d+" %))]
    (zipmap (->longs times)
            (->longs distances))))

(defn compute-distance-traveled
  [race-time hold-time]
  (* hold-time (- race-time hold-time)))

(defn ways-to-win
  [[race-time best-distance]]
  (->> (range race-time)
       (keep
         (fn [hold-time]
           (let [distance (compute-distance-traveled race-time hold-time)]
             (when (> distance best-distance)
               [hold-time distance]))))))

(defn solve-part-one
  [puzzle-input]
  (->> (parse-input puzzle-input)
       (map (comp count ways-to-win))
       (apply *)))


(defn parse-input-part-two
  [input]
  (let [[times distances] (str/split-lines input)
        ->num (comp parse-long #(apply str %) #(re-seq #"\d+" %))]
    [(->num times)
     (->num distances)]))

(defn find-optimal-hold-time
  [[race-time _best-distance]]
  (loop [ix    (int (/ race-time 2))
         range (int (/ race-time 2))]
    (if (= 1 range)
      ix
      (let [center (compute-distance-traveled race-time ix)
            left   (compute-distance-traveled race-time (dec ix))
            right  (compute-distance-traveled race-time (inc ix))]
        (if (= center (max left right center))
          ix
          (recur
            (if (> left right)
              (int (- ix (/ range 2)))
              (int (+ ix (/ range 2))))
            (int (/ range 2))))))))

(defn find-winning-bound
  [side optimal [race-time best-distance]]
  (let [op (case side :left - :right +)]
    (loop [ix   optimal
           half (int (case side
                       :left  (int (math/ceil (/ optimal 2)))
                       :right (int (math/ceil (/ (- race-time optimal) 2)))))]
      (let [new-ix   (op ix half)
            dist     (compute-distance-traveled race-time new-ix)
            new-half (int (Math/ceil (/ half 2)))
            win?     (> dist best-distance)]
        (if (and (not win?) (= half new-half))
          ix
          (recur
            (if win?
              new-ix
              ix)
            new-half))))))

(defn find-winning-range
  [race]
  (let [optimal (find-optimal-hold-time race)]
    [(find-winning-bound :left optimal race)
     (find-winning-bound :right optimal race)]))

(defn solve-part-two
  [puzzle-input]
  (->> (parse-input-part-two puzzle-input)
       (find-winning-range)
       (reverse)
       (apply -)
       (inc)))


(comment
  (ways-to-win [30 200])
  (solve-part-one puzzle-input)
  (solve-part-two puzzle-input)
  )
