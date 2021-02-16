(ns cronstar.runtests
  (:require
   [cljs.test :as t :include-macros true]
   [cronstar.parser-test]
   [cronstar.match-test]
   [cronstar.generator-test]
   [cronstar.core-test]))

;; settings to support execution on Nashorn engine
(set! *print-fn* js/print)
(defmethod cljs.test/report [:cljs.test/default :end-run-tests]
                 [args]
  (when-not (t/successful? args)
    (js/exit 1)))


(t/run-tests 'cronstar.parser-test)
(t/run-tests 'cronstar.match-test)
(t/run-tests 'cronstar.generator-test)
(t/run-tests 'cronstar.core-test)
