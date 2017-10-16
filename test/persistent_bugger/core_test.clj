(ns persistent-bugger.core-test
  (:require [clojure.test :refer :all]
            [persistent-bugger.core :refer :all]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.clojure-test :refer [defspec]]
            [com.gfredericks.test.chuck :as chuck]
            [com.gfredericks.test.chuck.clojure-test :refer [checking]]))


; Max digits to prevent integer overflow can be calculated
; with this formula: (int (Math/log10 Integer/MAX_VALUE))
(def max-digits 9)

(def digits-gen
  (gen/vector
    (gen/choose 2 9)
    1
    max-digits))

(defn join-digits
  ([xs] (join-digits xs 0))
  ([[x & xs] n]
    (if-not (nil? x)
      (recur xs (+ (* 10 n) x))
      n)))

(deftest testing-persistence-subroutines
  (checking "One digit will return 1 for all 1 digit numbers" 100
    [n gen/pos-int]
    (is (if (< n 10)
            (one-digit? n)
            (not (one-digit? n)))))

  (checking "num to digits conversion" 100
    [digits digits-gen ]
    (let [n (join-digits digits)]
      (is (= (sort (num->digits n))
             (sort digits)))))

  (checking "multiply digits" 100
    [digits digits-gen]
    (let [multiple (reduce * 1 digits)]
      (is (= multiple
             (multiply-digits (join-digits digits)))))))

