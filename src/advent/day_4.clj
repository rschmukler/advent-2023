(ns advent.day-4
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defonce puzzle-input
  (line-seq (io/reader "resources/day_4.txt")))

(defn ->int
  [x]
  (Integer/parseInt x))

(defn line->card
  [line]
  (let [[header card-part] (str/split line #":")
        [winning-num-str
         player-num-str]   (str/split card-part #"\|")]
    {:id              (->int (re-find #"\d+" header))
     :winning-numbers (set (map ->int (re-seq #"\d+" winning-num-str)))
     :player-numbers  (map ->int (re-seq #"\d+" player-num-str))}))

(defn card->point-value
  "Return the value of the card"
  [{:keys [winning-numbers player-numbers]}]
  (let [match-count (count (filter winning-numbers player-numbers))]
    (if-not (pos? match-count)
      0
      (int (Math/pow 2 (dec match-count))))))


(defn solve-part-one
  "Return the solution to part one"
  [lines]
  (->> lines
       (map (comp card->point-value line->card))
       (apply +)))

(defn card->coppies
  [{:keys [winning-numbers player-numbers id]}]
  (range (inc id) (+ 1 id (count (filter winning-numbers player-numbers)))))

(defn solve-part-two
  [lines]
  (let [cards    (map line->card lines)
        id->card (into {} (for [{id :id :as card} cards]
                            [id card]))]
    (loop [stack  (vec cards)
           result 0]
      (if-not (seq stack)
        result
        (let [new-cards (keep id->card (card->coppies (peek stack)))]
          (recur
            (into (pop stack) new-cards)
            (inc result)))))))

(comment
  (solve-part-one puzzle-input)
  (solve-part-two puzzle-input))
