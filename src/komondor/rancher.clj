(ns komondor.rancher
  "Minimal Rancher API support for querying container/host details."
  (:require [clojure.tools.logging :as log]
            [clj-http.lite.client :as http]
            [clojure.data.json :as json]))

(defn- http-get
  "Make post for given Rancher endpoint. Should specify only part of url."
  ([config url opts]
     (let [r    (:rancher.endpoint config)
           opts (merge 
                 {:headers {"User-Agent" "Komondor 0.1.0"}}
                 (when (and (contains? config :rancher.api-access-key)
                            (contains? config :rancher.api-secret-key))
                   {:basic-auth [(:rancher.api-access-key config)
                                 (:rancher.api-secret-key config)]})
                 opts)
           ret (http/get (str r url) opts)]
       (if (= 200 (:status ret))
         (json/read-str (:body ret))
         (log/error "Failed to query" url "Got reply:" ret))))
  ([config url] (http-post config url nil)))

(defn- collect-envs
  "Collect all environments."
  [config]
  (let [ret (http-get config "/projects")]
    (map #(get % "id") (get ret "data"))))

(defn- collect-hosts
  "Collect hosts from given environment."
  [config env]
  (let [ret (http-get config (str "/projects/" env "/hosts/"))]
    (reduce (fn [coll item]
              (let [o (get item "links")]
                (assoc coll (get item "id")
                       [(get item "hostname")
                        (get o "stats")
                        (get o "hostStats")
                        (get o "containerStats")])))
            {}
            (get ret "data"))))
