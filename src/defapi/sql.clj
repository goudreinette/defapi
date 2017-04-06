(ns defapi.sql
  (:require [clojure.java.jdbc :as j]
            [clojure.core.match :refer [match]]))



(defn sql-resolver [db]
  (fn [table attrs columns]
    (j/query db
      (str "SELECT " (clojure.string/join ", " columns) " FROM " table))))

