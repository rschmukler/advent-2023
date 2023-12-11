(ns advent.day-11-test
  (:require [advent.day-11 :as sut]
            [clojure.test :refer [deftest testing is]]
            [clojure.java.io :as io]))

(defonce sample-input
  (line-seq (io/reader "resources/day_11_sample.txt")))

(def sample-unexpaned-map
  (sut/lines->unexpanded-map sample-input))

(deftest lines->unexpanded-map-test
  (let [galaxy (sut/lines->unexpanded-map sample-input)]
    (is (= 9 (count galaxy)))
    (is (= 1 (:id (galaxy [3 0]))))))

(deftest find-empty-space-test
  (is (= {:columns [2 5 8]
          :rows    [3 7]}
         (sut/find-empty-space sample-unexpaned-map))))

(deftest expand-galaxy-test
  (let [galaxies (sut/expand-map sample-unexpaned-map)]
    (is (= 1 (:id (galaxies [4 0]))))
    (is (= 2 (:id (galaxies [9 1]))))
    (is (= 3 (:id (galaxies [0 2]))))))

(deftest galaxy-pairs-test
  (is (= 36 (count (sut/galaxy-pairs sample-unexpaned-map)))))

(deftest find-distance-test
  (let [galaxies   (sut/expand-map sample-unexpaned-map)
        id->galaxy (->> (vals galaxies)
                        (map (juxt :id identity))
                        (into {}))]
    (is (= 15 (sut/find-distance id->galaxy 1 7)))))

(deftest solve-test
  (is (= 374 (sut/solve 2 sample-input)))
  (is (= 1030 (sut/solve 10 sample-input)))
  (is (= 8410 (sut/solve 100 sample-input))))
