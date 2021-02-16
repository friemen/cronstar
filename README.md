# cronstar

Parse cron expression and generate a sequence of future DateTime
instances in Clojure or ClojureScript.

[![Build Status](https://travis-ci.org/friemen/cronstar.png?branch=master)](https://travis-ci.org/friemen/cronstar)

[![Clojars Project](http://clojars.org/cronstar/latest-version.svg)](http://clojars.org/cronstar)

[API docs](https://friemen.github.com/cronstar)


## Usage

This library can be used in conjunction with
[chime](https://github.com/jarohen/chime) to create custom schedulers.

See the [todoapp source
code](https://github.com/friemen/clj-todoapp-example) for an example
on how to create your own component-based scheduler where task
execution is controlled via cron expressions.

To use it in your own project add a dependency

```clojure
[cronstar "1.0.2"]
```

Here's how the main API function can be used:

```clojure
(require '[cronstar.core :as cron])
;= nil

(->> (cron/times "*/5 8-18 * * 1-5")
     (take 10))
;= (#object[org.joda.time.DateTime
;          "0x3045906"
;          "2020-02-17T08:00:00.000+01:00"]
;  #object[org.joda.time.DateTime
;          "0x5087dfe8"
;          "2020-02-17T08:05:00.000+01:00"]
;  #object[org.joda.time.DateTime
;          "0x4fca19c"
;          "2020-02-17T08:10:00.000+01:00"]
;  #object[org.joda.time.DateTime
;          "0x6aea4674"
;          "2020-02-17T08:15:00.000+01:00"]
;  #object[org.joda.time.DateTime
;          "0x48266d76"
;          "2020-02-17T08:20:00.000+01:00"]
;  #object[org.joda.time.DateTime
;          "0x44861a78"
;          "2020-02-17T08:25:00.000+01:00"]
;  #object[org.joda.time.DateTime
;          "0x2597a335"
;          "2020-02-17T08:30:00.000+01:00"]
;  #object[org.joda.time.DateTime
;          "0x1d768815"
;          "2020-02-17T08:35:00.000+01:00"]
;  #object[org.joda.time.DateTime
;          "0x66400b10"
;          "2020-02-17T08:40:00.000+01:00"]
;  #object[org.joda.time.DateTime
;          "0x1261987a"
;          "2020-02-17T08:45:00.000+01:00"])

```

Supported cron syntax:

A cron expression has 5 whitespace separated parts:

```
minute(0-59)  hour(0-59)  day-of-month(1-31)  month (1-12)  day-of-week (0-7)
```

Each part can be a list of comma separated numbers, ranges `from`'-'`to` or an
asterisk '*' character, which means any value is allowed.

A range or the '*' can be followed by '/'`stepsize`.


## Development

To start a REPL use `lein repl` and connect to it, for example via
CIDER.

The `user` namespace contains a function `cljs-repl` that starts a
REPL connection to Javas Nashorn JS Engine.

`lein test` compiles and executes tests on JVM.

`lein cljstest` compiles sources to JS and executes tests in Nashorn engine.


## License

Copyright © 2020 F.Riemenschneider

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.
