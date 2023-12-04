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
  "A naive first pass attempt"
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

(defn solve-part-two-fast
  "The thinking man's solution to part two"
  [lines]
  (let [all-cards (mapv line->card lines)]
    (loop [id->values {}
           cards      all-cards]
      (if-not (seq cards)
        (->> all-cards
             (keep (comp id->values :id))
             (apply +))
        (let [card     (peek cards)
              coppies  (card->coppies card)
              card-val (->> coppies
                            (keep id->values)
                            (apply +)
                            (inc))]
          (recur
            (assoc id->values (:id card) card-val)
            (pop cards)))))))

(comment
  (solve-part-one puzzle-input)
  (time
    (solve-part-two puzzle-input)) ;; 11.76 seconds :(
  (time
    (solve-part-two-fast puzzle-input)) ;; 2.03 milliseconds :D
  )
