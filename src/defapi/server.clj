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
  (let [mounted (mount-server (api config (apply hash-map (map eval executors))) {:port 8080})]
    `(do
       (def ~name ~mounted)
       (mount/start))))