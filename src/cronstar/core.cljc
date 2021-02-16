(ns cronstar.core
  "Main API functions for generating DateTime sequences."
  (:require
   #?(:clj [clj-time.core :as t]
      :cljs [cljs-time.core :as t])
   [cronstar.parser :as cron-parser]
   [cronstar.generator :as cron-gen]
   [cronstar.datetime :as cron-datetime]))


(defn times
  "Expects a `cron-expr` and an optional `datetime` and returns a
  sequence of DateTime instances. (DateTime instances either
  goog.date.DateTime in ClojureScript or org.joda.DateTime on JVM
  Clojure)."
  ([cron-expr]
   (times cron-expr (cron-datetime/*now-fn*)))

  ([cron-expr datetime]
   (let [schedule (cron-parser/parse cron-expr)]
     (->> (cron-datetime/to-timeinfo datetime)
          (cron-gen/datetime-seq schedule)
          (drop-while #(t/after? datetime %))))))
