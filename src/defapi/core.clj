(ns defapi.core
  (:use defapi.sql)
  (:require [instaparse.core :refer [defparser]]
            [clojure.core.match :refer [match]]))


(defparser query-parser
  "<query> =  <'{'> selection* <'}'>
   selection-set = identifier <'{'> selection* <'}'>
   <selection> =  selection-set | identifier
   <identifier> = #'\\w+'"
  :auto-whitespace :comma)


(defn- get-executor [executors key]
  (println executors)
  (get executors
    (keyword key)
    (executors :default)))

(defn execute-all [executors query-string]
  (apply merge
    (for [[_ key & children] (query-parser query-string)]
      {key
       ((get-executor executors key) key {} children)})))
