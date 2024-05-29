(ns user
  (:require
   [cljs.repl.node]
   [cider.piggieback]))

(defn cljs-repl
  []
  (cider.piggieback/cljs-repl (cljs.repl.node/repl-env)))
