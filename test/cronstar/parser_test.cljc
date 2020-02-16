(ns cronstar.parser-test
  (:require
   #?(:clj [clojure.test :refer :all])
   #?(:cljs [cljs.test :refer-macros [deftest testing are is run-tests]])
   [cronstar.parser :as cron-parser]))


(deftest test__parse-list
  (testing "Success"
    (are [k expr ranges]
        (= ranges (#'cron-parser/parse-list k expr))

      :minute "*"
      [{:from 0, :to 59, :step 1}]

      :minute "*/2"
      [{:from 0, :to 59, :step 2}]

      :minute "10-20/2"
      [{:from 10, :to 20, :step 2}]

      :minute "10-20/2,24-26"
      [{:from 10, :to 20, :step 2}
       {:from 24, :to 26, :step 1}]

      :day-of-week "*"
      [{:from 0, :to 7, :step 1}]

      ,,,))

  (testing "Exception is thrown"
    (is (thrown? #?(:clj Exception :cljs js/Error) (#'cron-parser/parse-list :minute nil)))
    (is (thrown? #?(:clj Exception :cljs js/Error) (#'cron-parser/parse-list :minute "")))
    (is (thrown? #?(:clj Exception :cljs js/Error) (#'cron-parser/parse-list :minute "x")))
    (is (thrown? #?(:clj Exception :cljs js/Error) (#'cron-parser/parse-list :minute "60")))
    ,,,))


(deftest test__parse
  (are [expr schedule]
      (= schedule (cron-parser/parse expr))
    "10,30  8-18/2  * *             1-5"
    {:minute      [{:from 10, :to 10, :step 1}
                   {:from 30, :to 30, :step 1}],
     :hour        [{:from 8, :to 18, :step 2}],
     :day         [{:from 1, :to 31, :step 1}],
     :month       [{:from 1, :to 12, :step 1}],
     :day-of-week [{:from 1, :to 5, :step 1}]}

    ,,,))
