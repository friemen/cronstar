(ns cronstar.core-test
  (:require
   [cronstar.core :as cronstar]
   #?(:clj [clojure.test :refer :all]
      :cljs [cljs.test :refer :all :include-macros true])
   #?(:clj [clj-time.core :as t]
      :cljs [cljs-time.core :as t])))

(deftest test__times
  (is (= 12
         (t/minute (first (cronstar/times "*/2 * * * *" (t/date-time 2021 2 16 14 10 40))))))
  (is (= 10
         (t/minute (first (cronstar/times "*/2 * * * *" (t/date-time 2021 2 16 14 10 0)))))))
