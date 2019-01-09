# Net Promoter Score

[![Clojars Project](https://img.shields.io/clojars/v/net_promoter_score.svg)](https://clojars.org/net_promoter_score)

A pure clojure library for generating Net Promoter Score values

Add this project via Leiningen/Boot:

`[net_promoter_score "1.0.0"]`

Add this project via deps.edn:

`net_promoter_score {:mvn/version "1.0.0"}`

Gradle: `compile 'net_promoter_score:net_promoter_score:1.0.0'`

Maven:

```
<dependency>
  <groupId>net_promoter_score</groupId>
  <artifactId>net_promoter_score</artifactId>
  <version>1.0.0</version>
</dependency>
```
---

Usage:

The main functionality of this library is exposed via the `net_promoter_score.core`
namespace. Pulling that into your code is simple: 

`(:require [net_promoter_score.core :as nps])`

### Getting a Net Promoter Score

If you only need to retrieve a score for a list of numbers, you can pass all of the values to the
`get-promoter-score` function inside a vector. This will, by default, use the 6/9 cutoffs
that are used for sttandard NPS calculations.
```clojure
(nps/get-promoter-score [1 2 3 4 5 6 7 8 9 10])
=> -40
```

If, for some reason, you need to use custom values for your promoter/passive/detractor
calculations (maybe your marketing team wants to feel better about themselves?) You can pass
min/max values to `get-promoter-score` in order to reconfigure how it will calculate your NPS.

```clojure
(nps/get-promoter-score [1 2 3 4 5 6 7 8 9 10] 4 7)
=> 0
```

`get-promoter-score` does validate your inputs to make sure that they're within the 0-10
range that's standard for NPS scores. This can't be reconfigured.

```clojure
(nps/get-promoter-score [11 -1])
=> AssertionError
```

`get-promoter-score` will provide back a soft warning that your calculations are invalid
if you provide an empty vector.

```clojure
(nps/get-promoter-score [])
=> "N/A"
```

### Getting a statistical summary of a set of scores

If you'd like to dig a little deeper into your NPS values, picking up information about
the mean, median and standard deviation of your scores, `get-promoter-summary` should suit your needs.
It behaves similarly to `get-promoter score`, so to retrieve a summary of a vector of values: 
```clojure
(nps/get-promoter-summary [1 2 3 4 5 6 7 8 9 10])
=> {:score -40 :mean 5.5 :median 5.5 :standard-deviation 2.8722813232690143}
```

Setting custom boundaries for that same vector:

```clojure
(nps/get-promoter-summary [1 2 3 4 5 6 7 8 9 10] 4 7)
=> {:score 0 :mean 5.5 :median 5.5 :standard-deviation 2.8722813232690143}
```

Errors when provided invalid values
```clojure
(nps/get-promoter-summary [11 -1])
=> AssertionError
```

Soft warnings when provided an empty vector:

```clojure
(nps/get-promoter-summary [])
=> {:score "N/A" :mean "N/A" :median "N/A" :standard-deviation "N/A"}
```

### Getting a statistical summary of a set of scores based on the date those scores were entered.

If you need to slice your data based on time windows, `get-promoter-summary-with-dates`
provides visibility into the statistical nature of your Net Promoter Score entries for the following time periods:

* All Time (The full population provided)
* The last 7 days, today inclusive
* The last 30 days, today inclusive
* 30-60 days ago
* 60-90 days ago

`get-promoter-summary-with-dates` does not take a vector of scores, but instead takes a vector
of hashmaps with keys of `:score` and `:date`. `:score` should be an integer representing a
NPS score (0-10). `:date` should be either a string representation of a date (default formatting
is `yyyy-MM-dd` or a java.time.LocalDate object). 

The API of `get-promoter-summary-with-dates` conforms to the same specifications of inputs
and outputs as `get-promoter-summary`. It returns a series of 5 summary hashmaps identical to the
output of `get-promoter-summary` for the specific windows as listed above.

Getting summaries for a list of values with dates:
```clojure
(nps/get-promoter-summary-with-dates [{:score 1 :date ...}])
=> {:all-time {...} :last-seven {...} :last-thirty {...} :thirty-to-sixty {...} :sixty-to-ninety {...} }
```

It validates inputs as expected, will return `"N/A"` for time periods with no entries, etc.