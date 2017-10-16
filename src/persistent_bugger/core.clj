(ns persistent-bugger.core)

(defn one-digit?
  [n]
  (< n 10))

(defn num->digits
  ([n] (num->digits n []))
  ([n digits]
    (if (one-digit? n)
      (conj digits n)
      (let [next-digit (mod n 10)
            next-n     (quot n 10)]
        (recur next-n (conj digits next-digit))))))

(defn multiply-digits
  [n]
  (->> n
    num->digits
    (reduce * 1)))

(defn persistence
  [n]
  (->> n
    (iterate multiply-digits)
    (take-while (comp not one-digit?))
    count))

