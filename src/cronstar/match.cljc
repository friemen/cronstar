(ns cronstar.match
  (:require
   [clojure.string :as str]
   [cronstar.datetime :as datetime]
   [cronstar.parser :as parser]))


(defn expression->matcher
  [field-keys cron-expr]
  (let [constraints
        (for [[k ranges] (parser/parse field-keys cron-expr)
              r          ranges]
          [k r])]
    (fn [datetime]
      (let [timeinfo (datetime/to-timeinfo datetime)]
        (every? (fn [[k {:keys [from to step]}]]
                  (let [val (get timeinfo k)]
                    (and (<= from val to)
                         (= 0 (rem (+ val from) step)))))
                constraints)))))

(comment

  ((expression->matcher [:second :minute] "*/3 1-30/2") "2020-02-11 22:15:00")

  ,,,)
