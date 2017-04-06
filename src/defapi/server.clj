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
(defn api [config resolvers]
  (wrap-json-response
    (POST "/" {query-string :body}
      {:body (->> query-string slurp (resolve-all resolvers))})))

(defmacro defapi [name config & resolvers]
  (let [mounted (mount-server (api config (apply hash-map (map eval resolvers))) {:port 8080})]
    `(do
       (def ~name ~mounted)
       (mount/start))))