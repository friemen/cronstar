(ns cronstar.generator-test
  (:require
   #?(:clj  [clojure.test :refer :all])
   #?(:cljs [cljs.test :refer-macros [deftest is are testing run-tests]])
   [cronstar.generator :as cron-gen]
   [cronstar.parser :as cron-parser]))


(deftest test__time-seq
  (are [cron-expr timeinfo n times]
      (= times (->> timeinfo
                    (cron-gen/time-seq (cron-parser/parse cron-expr))
                    (take n)))

    "*/30 10-12 * * *"
    {:minute 42 :hour 8}
    8
    '({:minute 0, :hour 10}
      {:minute 30, :hour 10}
      {:minute 0, :hour 11}
      {:minute 30, :hour 11}
      {:minute 0, :hour 12}
      {:minute 30, :hour 12})

    "*/5 */2 * * *"
    {:minute 57 :hour 16}
    2
    '({:minute 0, :hour 18}
      {:minute 5, :hour 18})

    "*/5 */2 * * *"
    {:minute 57 :hour 23}
    1
    '()))


(deftest test__date-seq
  (are [cron-expr timeinfo n times]
      (= times (->> timeinfo
                    (cron-gen/date-seq (cron-parser/parse cron-expr))
                    (take n)))

    "0 0 * * *"
    {:day 28 :month 2 :year 2020}
    3
    '({:minute 0, :hour 0, :day-of-week 5, :day 28, :month 2, :year 2020}
      {:minute 0, :hour 0, :day-of-week 6, :day 29, :month 2, :year 2020}
      {:minute 0, :hour 0, :day-of-week 7, :day 1, :month 3, :year 2020})

    "0 0 * * 1-5"
    {:day 29 :month 2 :year 2020}
    1
    '({:minute 0, :hour 0, :day-of-week 1, :day 2, :month 3, :year 2020})))


(deftest test__timeinfo-seq
  (are [cron-expr timeinfo n times]
      (= times (->> timeinfo
                    (cron-gen/timeinfo-seq (cron-parser/parse cron-expr))
                    (take n)))

    "5 20 * * *"
    {:minute 43, :hour 20, :day-of-week 6, :day 15, :month 2, :year 2020}
    1
    '({:minute 5, :hour 20, :day-of-week 7, :day 16, :month 2, :year 2020})

    "*/5 */2 * * 1-5"
    {:minute 57 :hour 23 :day 29 :month 2 :year 2020}
    1
    '({:minute 0, :hour 0, :day-of-week 1, :day 2, :month 3, :year 2020})

    "*/5 */2 * * 1-5"
    {:minute 57 :hour 14 :day 28 :month 2 :year 2020}
    3
    '({:minute 0, :hour 16, :day-of-week 5, :day 28, :month 2, :year 2020}
      {:minute 5, :hour 16, :day-of-week 5, :day 28, :month 2, :year 2020}
      {:minute 10, :hour 16, :day-of-week 5, :day 28, :month 2, :year 2020})

    "*/20 */2 * * 1-5"
    {:minute 57 :hour 21 :day 28 :month 2 :year 2020}
    4
    '({:minute 0, :hour 22, :day-of-week 5, :day 28, :month 2, :year 2020}
      {:minute 20, :hour 22, :day-of-week 5, :day 28, :month 2, :year 2020}
      {:minute 40, :hour 22, :day-of-week 5, :day 28, :month 2, :year 2020}
      {:minute 0, :hour 0, :day-of-week 1, :day 2, :month 3, :year 2020})))
