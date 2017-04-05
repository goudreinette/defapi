(ns defapi.sql
  (:require [clojure.java.jdbc :as j]
            [clojure.core.match :refer [match]]))


(def db {:dbtype   "mysql"
         :dbname   "portfolio"
         :user     "root"
         :password ""})

(defn- query->sql [query]
  (match query
    ([:object first & rest] :seq)
    (str "SELECT " (query->sql rest) " FROM " (query->sql first))
    [:identifier x]
    x
    ([& rest] :seq)
    (clojure.string/join ", " (map query->sql rest))))


(defn sql-executor [query]
  (j/query db (query->sql query)))

