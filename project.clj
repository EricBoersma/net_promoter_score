(defproject net_promoter_score "1.0.0"
  :description "A pure clojure library for calculating net promoter score values."
  :url "https://github.com/EricBoersma/net_promoter_score"
  :license {:name "MIT license"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [
                  [org.clojure/clojure "1.9.0"]
                  [clojure.java-time "0.3.2"]
                  [org.clojure/math.numeric-tower "0.0.4"]]
  :repl-options {:init-ns net-promoter-score.core})
