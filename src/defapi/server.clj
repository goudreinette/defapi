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
    :stop (server))
  #_(mount/start))


; API
(defn api [resolvers]
  (wrap-json-response
    (POST "/" {query-string :body}
      {:body (->> query-string slurp (resolve-all resolvers))})))

(defmacro defapi [name default & resolvers]
  (let [mounted (mount-server (api (apply hash-map :default (eval default) (map eval resolvers))) {:port 8080})]
    `(do
       (def ~name ~mounted)
       (mount/start))))