(ns advent.day-8-test
  (:require [advent.day-8 :as sut]
            [clojure.test :refer [deftest testing is]]
            [clojure.java.io :as io]))

(def sample-input
  (line-seq (io/reader "resources/day_8_sample.txt")))

(deftest line->node-test
  (is (= {:id    "AAA"
          \L  "BBB"
          \R "BBB"}
         (sut/line->node "AAA = (BBB, BBB)"))))

(deftest nodes->map
  (is (= {"AAA" {:id "AAA" \L "BBB" \R "BBB"}}
         (sut/nodes->map
           [{:id    "AAA"
             \L  "BBB"
             \R "BBB"}]))))

(deftest solve-part-one-test
  (is (= 2 (sut/solve-part-one sample-input))))

(def part-two-sample-input
  (line-seq (io/reader "resources/day_8_part_2_sample.txt")))

(deftest find-starting-nodes-test
  (is (= [{:id "11A" \L "11B" \R "XXX"}
          {:id "22A" \L "22B" \R "XXX"}]
         (sut/find-starting-nodes
           (-> part-two-sample-input
               (sut/parse-input)
               (second))))))

(deftest solve-part-two-test
  (is (= 6 (sut/solve-part-two part-two-sample-input))))
