(ns defapi.core
  (:use defapi.sql)
  (:require [instaparse.core :as insta :refer [defparser]]
            [clojure.core.match :refer [match]]))


(defparser query-parser
  "set = <''> | <'{'> selector* <'}'>
   selector = identifier arguments set
   arguments = <''> | <'('> argument* <')'>
   <argument> = identifier <':'> identifier
   <identifier> = #'\\w+'"
  :auto-whitespace :comma)

(def query-transformer
  (partial insta/transform
    {:selector  vector
     :arguments hash-map
     :set       vector}))

(def parse (comp query-transformer query-parser))


(defn- get-resolver [resolvers key]
  (get resolvers
    (keyword key)
    (resolvers :default)))

(defn resolve-all [resolvers query-string]
  (apply merge
    (for [[key args children] (parse query-string)]
      {key
       ((get-resolver resolvers key) key args children)})))
