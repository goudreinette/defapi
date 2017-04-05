(ns defapi.sql
  (:require [clojure.java.jdbc :as j]
            [clojure.core.match :refer [match]]))


(def db {:dbtype   "mysql"
         :dbname   "portfolio"
         :user     "root"
         :password ""})

(defn query->sql [query]
  (match query
    ([:selection-set first & rest] :seq)
    (str "SELECT " (query->sql rest) " FROM " (query->sql first))

    ([& rest] :seq)
    (clojure.string/join ", " (map query->sql rest))

    x
    x))


(defn execute-sql [query]
  (j/query db (query->sql query)))

