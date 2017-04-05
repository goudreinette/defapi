(ns defapi.core
  (:use defapi.sql)
  (:refer-clojure :exclude [resolve])
  (:require [instaparse.core :refer [defparser]]
            [clojure.core.match :refer [match]]))


(defparser query-parser
  "<query> = <'{'> selection* <'}'>
   <selection> = identifier | object
   object = identifier query
   identifier = #'\\w+'"
  :auto-whitespace :comma)

(defn- query-key [query]
  (match query
    ([:object [:identifier x] & rest] :seq) x))

(defmulti resolver query-key)

(defmethod resolver :default [query]
  (sql-executor query))

(defn resolve-all [query-string]
  (apply merge
    (for [query (query-parser query-string)]
      {(query-key query)
       (resolver query)})))
