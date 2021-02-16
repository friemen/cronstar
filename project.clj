(defproject cronstar "1.0.1-SNAPSHOT"
  :description
  "A cron expression parser and datetime sequence generator"

  :url
  "https://github.com/friemen/cronstar"

  :license
  {:name "EPL-2.0"
   :url  "https://www.eclipse.org/legal/epl-2.0/"}

  :dependencies
  [[org.clojure/clojure "1.9.0"]
   [clj-time "0.15.2"]
   [com.andrewmcveigh/cljs-time "0.5.2"]]

  :aliases
  {"cljstest" ["with-profile" "cljs" "cljsbuild" "test" "unittests"]}

  :plugins
  [[lein-codox "0.10.7"]]

  :codox
  {:defaults                  {}
   :sources                   ["src"]
   :output-path               "docs"
   :exclude                   []
   :src-dir-uri               "https://github.com/friemen/cronstar/blob/master/"
   :src-linenum-anchor-prefix "L"}

  :profiles
  {:dev
   {:source-paths
    ["dev"]
    :dependencies
    [[org.clojure/clojurescript "1.9.946"]
     [cider/piggieback "0.4.2"]]
    :repl-options
    {:nrepl-middleware [cider.piggieback/wrap-cljs-repl]}}

   :cljs
   {:dependencies
    [[org.clojure/clojurescript "1.9.946"]]

    :plugins
    [[lein-cljsbuild "1.1.7"]]

    :hooks
    [leiningen.cljsbuild]

    :cljsbuild
    {:builds [{:source-paths
               ["src" "test"]
               :compiler
               {:output-to     "target/cljsbuild-main.js"
                :optimizations :whitespace
                :pretty-print  true}}]

     :test-commands
     ;; run with `lein with-profile cljs cljsbuild test unittests`
     {"unittests" ["jjs" "target/cljsbuild-main.js"]}}}}

  :scm
  {:name "git"
   :url  "https://github.com/friemen/cronstar"}

  :repositories
  [["clojars" {:url   "https://clojars.org/repo"
               :creds :gpg}]])
