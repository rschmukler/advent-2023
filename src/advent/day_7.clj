(ns advent.day-7
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defonce puzzle-input
  (line-seq (io/reader "resources/day_7.txt")))

(def card->val
  {\A 14
   \K 13
   \Q 12
   \J 11
   \T 10
   \9 9
   \8 8
   \7 7
   \6 6
   \5 5
   \4 4
   \3 3
   \2 2})

(def type->val
  {:five-of-a-kind  7
   :four-of-a-kind  6
   :full-house      5
   :three-of-a-kind 4
   :two-pair        3
   :pair            2
   :high-card       1})

(defn parse-hand
  ([hand]
   (parse-hand false hand))
  ([jokers-wild? hand]
   (let [[freqs
          joker-count] (let [freqs (frequencies hand)]
                         (if jokers-wild?
                           [(dissoc freqs \J) (get freqs \J 0)]
                           [freqs 0]))
         [a-count
          b-count]     (->> freqs
                            (vals)
                            (sort >)
                            (into []))
         a-count       (or a-count 0)
         b-count       (or b-count 0)]
     (case (+ a-count joker-count)
       5 [:five-of-a-kind hand]
       4 [:four-of-a-kind hand]
       3 (if (= 2 b-count)
           [:full-house hand]
           [:three-of-a-kind hand])
       2 (if (= 2 b-count)
           [:two-pair hand]
           [:pair hand])
       1 [:high-card hand]))))

(defn line->hand+bid
  [jokers-wild? line]
  (let [[hand bid] (str/split line #" ")]
    [(parse-hand jokers-wild? hand) (parse-long bid)]))

(defn compare-hands
  [jokers-wild? [a-type a-hand] [b-type b-hand]]
  (let [card->val     (if jokers-wild?
                        (assoc card->val \J 1)
                        card->val)
        val-of-type-a (type->val a-type)
        val-of-type-b (type->val b-type)]
    (if-not (= val-of-type-a val-of-type-b)
      (compare val-of-type-a val-of-type-b)
      (compare
        (->> a-hand (mapv card->val))
        (->> b-hand (mapv card->val))))))

(defn solve
  [input & {:keys [jokers-wild?]}]
  (let [hands     (map (partial line->hand+bid jokers-wild?) input)
        hand->bid (into {} hands)]
    (->> hand->bid
         (keys)
         (sort (partial compare-hands jokers-wild?))
         (map-indexed
           (fn [ix hand]
             (* (inc ix) (hand->bid hand))))
         (apply +))))

(comment
  (solve puzzle-input :jokers-wild? true))
