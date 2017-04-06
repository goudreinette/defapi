(ns user
  (:use defapi.core defapi.server defapi.sql))



(def repo {"user" "reinvdwoerd"
           "repo" "defapi"})

; Demo
(def sql (sql-resolver {:dbtype   "mysql"
                        :dbname   "portfolio"
                        :user     "root"
                        :password ""}))


(defn github-resolver [key args children]
  (select-keys repo children))

; ---
(defapi portfolio-api sql
  :github github-resolver)

(mount-api portfolio-api)