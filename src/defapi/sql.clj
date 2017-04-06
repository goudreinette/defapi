(ns defapi.sql
  (:require [clojure.java.jdbc :as j]
            [clojure.core.match :refer [match]]
            [clojure.string :refer [join]]
            [clojure.pprint :refer [pprint]]))

(defn attr= [[k v]]
  (str k " = " v))

(defn where [attrs]
  (if attrs
    (str " WHERE " (join " AND " (map attr= attrs)))))

(defn sql-resolver [db]
  (fn [table attrs columns]
    (pprint (str "SELECT " (join ", " columns) " FROM " table (where attrs)))

    (j/query db
      (str "SELECT " (join ", " columns) " FROM " table (where attrs)))))

