(ns user
  (:use defapi.server defapi.sql))


; Demo
(defn github-resolver [_ _ _]
  {:user "reinvdwoerd"
   :repo "defapi"})

(defapi portfolio-api db
  :default sql-resolver
  :github github-resolver)