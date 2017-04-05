(ns defapi.server
  (:use org.httpkit.server defapi.core)
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
(defn api [config resolvers]
  (wrap-json-response
    (POST "/" {query-string :body}
      {:body (-> query-string slurp resolve-all)})))

(defmacro defapi [name config & resolvers]
  `(do
     (def ~name (mount-server ~(api config resolvers) {:port 8080}))
     (mount/start)))

; Demo
(defapi portfolio-api db)