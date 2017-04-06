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


(defn- get-resolver [resolvers key]
  (println resolvers)
  (get resolvers
    (keyword key)
    (resolvers :default)))

(defn resolve-all [resolvers query-string]
  (apply merge
    (for [[_ key & children] (query-parser query-string)]
      {key
       ((get-resolver resolvers key) key {} children)})))
