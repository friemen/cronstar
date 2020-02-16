(ns user
  (:require
   [cider.piggieback]
   [cljs.repl.nashorn]))


(defn cljs-repl
  []
  (cider.piggieback/cljs-repl (cljs.repl.nashorn/repl-env)))
