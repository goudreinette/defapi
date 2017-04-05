(ns defapi.core
  (:use defapi.sql)
  (:require [instaparse.core :refer [defparser]]
            [clojure.core.match :refer [match]]))


(defparser query-parser
  "<query> =  <'{'> selection* <'}'>
   selection-set = identifier <'{'> selection* <'}'>
   <selection> =  selection-set | identifier
   <identifier> = #'\\w+'"
  :auto-whitespace :comma)

(defn- query-key [query]
  (match query
    ([:selection-set x & rest] :seq) x))

(defn- get-executor [executors query]
  (println executors)
  (get executors
    (keyword (query-key query))
    (executors :default)))

(defn execute-all [executors query-string]
  (apply merge
    (for [query (query-parser query-string)]
      {(query-key query)
       (apply (get-executor executors query) (rest query))})))
