(ns advent.day-10-test
  (:require [advent.day-10 :as sut]
            [clojure.test :refer [deftest testing is]]
            [clojure.java.io :as io]))


(def sample-input
  (line-seq (io/reader "resources/day_10_sample.txt")))

(def sample-map
  (sut/input->map sample-input))

(deftest find-start-test
  (is (= [1 1] (sut/find-start sample-map))))

(deftest valid-neighbors-test
  (is (= [[3 1]
          [1 1]]
         (sut/valid-neighbors sample-map [2 1]))))

(deftest infer-start-test
  (let [{:keys [south? east? west? north?]}
        (get sample-map [1 1])]
    (is east?)
    (is south?)
    (is (not north?))
    (is (not west?))))

(deftest find-loop-test
  (is (= 8
         (count (sut/find-loop sample-map)))))

(deftest solve-part-one-test
  (is (= 4
         (sut/solve-part-one sample-input))))

(def small-enclosed-example
  (->> "resources/day_10_sample_small_loop.txt"
       (io/reader)
       (line-seq)
       (sut/input->map)))

(def small-squeeze-example
  (->> "resources/day_10_small_squeeze_example.txt"
       (io/reader)
       (line-seq)
       (sut/input->map)))

(deftest squeeze-output-test
  (is (= [8 6]
         (sut/squeeze-output small-enclosed-example [2 6]))))

(deftest enclosed-tiles-test
  (testing "simple small"
    (let [enclosed-sets (sut/enclosed-tiles small-enclosed-example)]
      (is (= 2 (count enclosed-sets)))
      (is (= 4 (count (apply concat enclosed-sets))))))

  (testing "squeeze small"
    (let [enclosed-sets (sut/enclosed-tiles small-squeeze-example)]
      (is (= 3 (count enclosed-sets)))
      (is (= 4 (count (apply concat enclosed-sets)))))))

(deftest solve-part-two-test
  (is (= 4 (sut/solve-part-two))))
