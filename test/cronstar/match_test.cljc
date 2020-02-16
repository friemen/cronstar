(ns cronstar.match-test
  (:require
   #?(:clj [clojure.test :refer :all])
   #?(:cljs [cljs.test :refer-macros [deftest are is testing run-tests]])
   [cronstar.match :as match]))


(deftest test__expression->matcher
  (are [ks expression ts result]
      (= result ((match/expression->matcher ks expression) ts))

      [:minute :hour]
      "22-24 */2"
      "2019-09-20 20:23:00"
      true

      [:minute :hour :day :month :day-of-week]
      "0 17 * * *"
      "2019-09-20 20:23:00"
      false

      [:minute :hour :day :month :day-of-week]
      "0 17 * * *"
      "2019-09-20 17:0:00"
      true))
