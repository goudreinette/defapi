(ns defapi.server
  (:use org.httpkit.server defapi.core defapi.sql)
  (:require [mount.core :as mount :refer [defstate]]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [mount.core :as mount]
            [ring.middleware.json :refer [wrap-json-response]])
  (:refer-clojure :exclude [resolve]))


; Mount
(defn mount-server [handler args]
  (defstate server
    :start (run-server handler args)
    :stop (server)))


; API
(defn api [config executors]
  (wrap-json-response
    (POST "/" {query-string :body}
      {:body (->> query-string slurp (execute-all executors))})))

(defmacro defapi [name config & executors]
  `(do
     (def ~name (mount-server ~(api config executors) {:port 8080}))
     (mount/start)))

; Demo
(defapi portfolio-api db
  :default execute-sql)