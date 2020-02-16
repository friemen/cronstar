(ns cronstar.generator
  "A generator for infinite datetime sequences on the basis of schedules."
  (:require
   #?(:clj [clj-time.core :as t])
   #?(:cljs [cljs-time.core :as t])
   [cronstar.datetime :as datetime]))


(defn- ranges->seq
  [schedule k]
  (let [ranges (k schedule)]
    (->> ranges
         (mapcat (fn [{:keys [from to step]}]
                   (range from (inc to) step)))
         (sort)
         (distinct))))

(comment
  (ranges->seq {:day [{:from 1 :to 10 :step 3}
                      {:from 4 :to 5 :step 1}]}
               :day)
  ,,,)


(defn- date-pred
  [schedule]
  (let [allowed-days         (set (ranges->seq schedule :day))
        allowed-months       (set (ranges->seq schedule :month))
        allowed-days-of-week (set (ranges->seq schedule :day-of-week))]
    (fn [{:keys [day day-of-week month] :as timeinfo}]
      (boolean
       (and (allowed-days day)
            (allowed-days-of-week day-of-week)
            (allowed-months month))))))

;; Public API

(defn date-seq
  [{:keys [day month] :as schedule} timeinfo]
  (let [allowed-date? (date-pred schedule)]
    (->> timeinfo
         (datetime/to-timeinfo)
         (datetime/from-timeinfo)
         (iterate (fn [dt]
                    (t/plus dt (t/days 1))))
         (map datetime/to-timeinfo)
         (filter allowed-date?))))



(defn time-seq
  [schedule timeinfo]
  (->> (for [hour   (ranges->seq schedule :hour)
             minute (ranges->seq schedule :minute)]
         {:hour hour
          :minute minute})
       (drop-while #(datetime/before? % timeinfo))))


(defn timeinfo-seq
  [schedule timeinfo]
  (let [[first-date & more-dates :as dates]
        (date-seq schedule timeinfo)

        first-date-on-timeinfo?
        (= (select-keys timeinfo [:year :month :day])
           (select-keys first-date [:year :month :day]))

        times-on-timeinfo-day
        (if first-date-on-timeinfo?
          (for [time (time-seq schedule (select-keys timeinfo [:minute :hour]))]
            (merge first-date time)))

        times-on-more-days
        (for [date (if first-date-on-timeinfo? more-dates dates)
              time (time-seq schedule {:minute 0 :hour 0})]
          (merge date time))]
    (concat times-on-timeinfo-day times-on-more-days)))


(defn datetime-seq
  [schedule timeinfo]
  (->> (timeinfo-seq schedule timeinfo)
       (map datetime/from-timeinfo)))



;; In the REPL

(comment

  (require '[cronstar.parser :as cron-parser])

  (def schedule
    (cron-parser/parse [:minute   :hour    :day             :month    :day-of-week]
                       "13,42     */10     1-10/2,22,9-13   *         *"))

  (def current-timeinfo
    {:minute 14 :hour 21 :day 12 :month 2 :year 2020})

  (->> (time-seq schedule current-timeinfo)
       (take 20))

  (->> (datetime-seq schedule (datetime/to-timeinfo (datetime/*now-fn*)))
       (take 10))

  (->> (timeinfo-seq (cron-parser/parse "*/20 */2 * * 1-5")
                     {:minute 57 :hour 21 :day 28 :month 2 :year 2020})
       (take 10))

  ,,,)
