(ns defapi.core
  (:use defapi.sql)
  (:require [instaparse.core :refer [defparser]]
            [clojure.core.match :refer [match]]))


(defparser query-parser
  "<query> =  <'{'> selection* <'}'>
   selection-set = identifier arguments <'{'> selection* <'}'>
   arguments = <''> | <'('> argument* <')'>
   <argument> = identifier <':'> identifier
   <selection> =  selection-set | identifier
   <identifier> = #'\\w+'"
  :auto-whitespace :comma)


(defn- get-resolver [resolvers key]
  (get resolvers
    (keyword key)
    (resolvers :default)))

(defn resolve-all [resolvers query-string]
  (apply merge
    (for [[_ key [_ & {:as arguments :or {}}] & children] (query-parser query-string)]
      {key
       ((get-resolver resolvers key) key arguments children)})))
