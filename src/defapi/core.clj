(ns defapi.core
  (:use defapi.sql)
  (:require [instaparse.core :refer [defparser]]
            [clojure.core.match :refer [match]]))


(defparser query-parser
  "<query> = <'{'> selection* <'}'>
   children = <''> | <'{'> selection* <'}'>
   selection = identifier arguments children
   arguments = <''> | <'('> argument* <')'>
   <argument> = identifier <':'> identifier
   <identifier> = #'\\w+'"
  :auto-whitespace :comma)

(defn transform-parsed-query [query]
  (match query
    ([:selection key [:arguments & args] [:children & children]] :seq)
    [key (apply hash-map args) (map transform-parsed-query children)]))



(defn- get-resolver [resolvers key]
  (get resolvers
    (keyword key)
    (resolvers :default)))

(defn resolve-all [resolvers query-string]
  (apply merge
    (for [[_ key [_ & {:as arguments :or {}}] & children] (query-parser query-string)]
      {key
       ((get-resolver resolvers key) key arguments children)})))
