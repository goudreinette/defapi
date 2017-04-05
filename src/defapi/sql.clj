(ns defapi.sql
  (:require [clojure.java.jdbc :as j]
            [clojure.core.match :refer [match]]))


(def db {:dbtype   "mysql"
         :dbname   "portfolio"
         :user     "root"
         :password ""})


(defn execute-sql [table & columns]
  (j/query db
    (str "SELECT " (clojure.string/join ", " columns) " FROM " table)))

