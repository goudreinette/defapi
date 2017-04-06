(ns defapi.sql
  (:require [clojure.java.jdbc :as j]
            [clojure.core.match :refer [match]]
            [clojure.string :refer [join]]
            [clojure.pprint :refer [pprint]]))


(defn where [attrs]
  (if (not-empty attrs)
    (str " WHERE " (join " AND " (for [[k v] attrs]
                                   (str k " = " v))))))

(defn sql-resolver [db]
  (fn [table attrs columns]
    (pprint (str "SELECT " (join ", " (map first columns)) " FROM " table (where attrs)))

    (j/query db
      (str "SELECT " (join ", " (map first columns)) " FROM " table (where attrs)))))

