(ns cronstar.datetime
  "Datetime related utilities."
  #?(:clj (:require
            [clj-time.core :as t]
            [clj-time.coerce :as tc]
            [clj-time.format :as tf]))
  #?(:cljs (:require
            [cljs-time.core :as t]
            [cljs-time.coerce :as tc]
            [cljs-time.format :as tf])))


(def bounds
  {:year        {:min-value 1970 :max-value 9999}
   :day-of-week {:min-value 0 :max-value 7}   ;; 0 and 7 are Sunday
   :month       {:min-value 1 :max-value 12}
   :day         {:min-value 1 :max-value 31}
   :hour        {:min-value 0 :max-value 23}
   :minute      {:min-value 0 :max-value 59}
   :second      {:min-value 0 :max-value 59}})

(def field-keys
  [:minute :hour :day :month :day-of-week])



#?(:clj
   (defn- dispatch-date
     [x]
     (cond
       (instance? java.util.Date x)         :date
       (instance? org.joda.time.DateTime x) :datetime
       (string? x)                          :string
       (map? x)                             :default)))


#?(:cljs
   (defn- dispatch-date
     [x]
     (cond
       (instance? js/Date x)            :date
       (instance? goog.date.DateTime x) :datetime
       (string? x)                      :string
       (map? x)                         :default)))


(defmulti to-timeinfo dispatch-date)


(defmethod to-timeinfo :default
  [timeinfo]
  timeinfo)


(defmethod to-timeinfo :string
  [s]
  (to-timeinfo (tf/parse (tf/formatter "yyyy-MM-dd HH:mm:ss") s)))


(defmethod to-timeinfo :date
  [d]
  (let [dt (tc/from-date d)]
    (to-timeinfo dt)))


(defmethod to-timeinfo :datetime
  [dt]
  {:minute      (t/minute dt)
   :hour        (t/hour dt)
   :day-of-week (t/day-of-week dt)
   :day         (t/day dt)
   :month       (t/month dt)
   :year        (t/year dt)})


(def ^:dynamic *now-fn*
  "Returns the current time as Joda DateTime in default time zone."
  #?(:clj #(t/to-time-zone (t/now) (t/default-time-zone)))
  #?(:cljs #(t/to-default-time-zone (t/now))))


(defn from-timeinfo
  [{:keys [year month day hour minute]
    :or   {hour   0
           minute 0}}]
  (let [dt (t/date-time year month day hour minute 0)]
    #?(:clj (t/from-time-zone dt (t/default-time-zone)))
    #?(:cljs (t/from-default-time-zone dt))))


(defn before?
  "Returns a truthy value when timeinfo `this` represents an instant
  before timeinfo `that`."
  [this that]
  (let [first-difference
        (->> [:year :month :day :hour :minute]
             (map #(vector (get this % 0) (get that % 0)))
             (drop-while (partial apply =))
             (first))]
    (if first-difference
      (apply < first-difference))))
