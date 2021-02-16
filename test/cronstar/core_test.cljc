(ns cronstar.core-test
  #?(:clj
     (:require
      [clojure.test :refer :all]
      [clj-time.core :as t]
      [cronstar.core :as cronstar])
     :cljs
     (:require
      [cljs.test :refer-macros [deftest is are testing run-tests]]
      [cljs-time.core :as t]
      [cronstar.core :as cronstar])))

(deftest test__times
  (is (= 12
         (t/minute (first (cronstar/times "*/2 * * * *" (t/date-time 2021 2 16 14 10 40))))))
  (is (= 10
         (t/minute (first (cronstar/times "*/2 * * * *" (t/date-time 2021 2 16 14 10 0)))))))
