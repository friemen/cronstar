(ns cronstar.parser
  "A parser for cron expressions."
  (:require
   [clojure.string :as str]
   [cronstar.datetime :as datetime]))

;; See
;; https://www.unix.com/man-page/linux/5/crontab

;; conditions resulting from day (of month) and day-of-week
;; expressions are combined with OR!


(defn- ->int
  [s]
  (if s
    #?(:clj (Integer/parseInt s))
    #?(:cljs (js/parseInt s))))


(defn- check-bounds
  [k n]
  (if n
    (let [{:keys [min-value max-value]} (datetime/bounds k)]
      (if (<= min-value n max-value)
        n
        (throw (ex-info "Value is not within bounds" {:field-key k
                                                      :value     n
                                                      :min-value min-value
                                                      :max-value max-value}))))))


(defn- parse-item
  [k s]
  (if-let [[s' from-str to-str val-str step-str]
           (re-matches #"(?:(?:(\d+)-(\d+))|(\d+)|\*)(?:/(\d+))?" s)]
    (if-let [val (->int val-str)]
      {:from (check-bounds k val)
       :to   val
       :step 1}
      (let [{:keys [min-value max-value]} (datetime/bounds k)]
        {:from (check-bounds k (or (->int from-str) min-value))
         :to   (check-bounds k (or (->int to-str) max-value))
         :step (or (->int step-str) 1)}))
    (throw (ex-info (str "String '" s "' cannot be parsed as cron expression part")
                    {:field-key k
                     :part s}))))


(defn- parse-list
  [k s]
  (->> (str/split s #",")
       (mapv (partial parse-item k))))


;; Public API

(defn parse
  "Returns a schedule map for a `cron-expr` whose parts correspond to
  minute, hour, day-of-month, month, day-of-week. Each part is a comma
  separated list of ranges or single numbers. Each range can be
  specified either as '*' or as '<min>-<max>' and may be followed by a
  '/<stepsize>' expression.

  Example: '0,30 8-18/2 * * 1-5' results in schedule

  {:minute      [{:from 10, :to 10, :step 1}
                 {:from 30, :to 30, :step 1}],
   :hour        [{:from 8, :to 18, :step 2}],
   :day         [{:from 1, :to 31, :step 1}],
   :month       [{:from 1, :to 12, :step 1}],
   :day-of-week [{:from 1, :to 5, :step 1}]}"

  ([cron-expr]
   (parse datetime/field-keys cron-expr))

  ([field-keys cron-expr]
   (let [items (str/split (str/trim cron-expr) #"\s+")]
     (when (< (count items) (count field-keys))
       (throw (ex-info "Cron expression string is not sufficient for key list"
                       {:expression cron-expr
                        :field-keys field-keys})))
     (->> items
          (map (fn [k cron-expr]
                 [k (parse-list k cron-expr)])
               field-keys)
          (into {})))))


;; In the REPL

(comment

  (parse [:minute :hour] "*/2 1-10/3")

  ,,,)
