(ns user
  (:use defapi.server defapi.sql))


; Demo
(defn execute-github [query]
  {:user "me"
   :repo "defapi"})

(defapi portfolio-api db
  :default execute-sql
  :github execute-github)