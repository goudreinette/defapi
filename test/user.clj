(ns user
  (:use defapi.server defapi.sql))


; Demo
(defn execute-github [_ {} user repo]
  {:user user
   :repo "defapi"})

(defapi portfolio-api db
  :default execute-sql
  :github execute-github)