(ns advent.day-2
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(defn line->game
  "Parse a line of text into a game"
  [line]
  (let [[game-header
         game-rounds] (str/split line #":")
        id            (Integer/parseInt (re-find #"\d+" game-header))]
    {:id id
     :rounds
     (vec
       (for [round (str/split game-rounds #";")]
         (into
           {}
           (for [items (map str/trim (str/split round #","))
                 :let  [[qty color] (str/split items  #" ")]]
             [(keyword color) (Integer/parseInt qty)]))))}))

(defn game->max-quantities
  "Take a game and return a map of maxiumum quantities used by the game"
  [game]
  (apply merge-with max (:rounds game)))

(defn possible?
  "Return whether the provided `game` was possible given the provided `qty-defenitions`"
  [qty-definitions game]
  (let [maxs (game->max-quantities game)]
    (every?
      (fn [[color qty]]
        (<= qty (get qty-definitions color 0)))
      maxs)))

(defn solve-part-one
  "Return the answer to part one using the provided lines"
  [qty-defs lines]
  (->> lines
       (map line->game)
       (filter (partial possible? qty-defs))
       (map :id)
       (apply +)))

(defn game->power
  "Convert a game to its power"
  [game]
  (->> game
       (game->max-quantities)
       (vals)
       (apply *)))

(defn solve-part-two
  "Return the answer to part two"
  [lines]
  (->> lines
       (map line->game)
       (map game->power)
       (apply +)))

(comment
  (->> (io/reader "resources/day_2.txt")
       line-seq
       (solve-part-one
         {:red   12
          :green 13
          :blue  14}))

  (->> (io/reader "resources/day_2.txt")
       line-seq
       (solve-part-two))
  )
