(ns komondor.core
  (:require [clj-yaml.core :as yaml]
            [clojure.tools.logging :as log]
            [clojure.java.io :as io]
            [clj-http.lite.client :as http])
  (:import org.apache.log4j.PropertyConfigurator
           java.util.Properties))

(defn- set-logfile-path!
  "Set location/name for log file. If path nil, does nothing."
  [path]
  (when path
    (let [props (Properties.)]
      (with-open [stream (-> path io/resource io/input-stream)]
        (.load props stream))
      (.setProperty props "log4j.appender.FILE.file" path)
      (PropertyConfigurator/configure props))))

(defn- start-listener
  "Scan for Rancher containers, init websocket listeners and
fire up connection to Riemann."
  [config]
  )

(defn- load-config
  "Read/parse configuration."
  [path]
  (try
    (-> path slurp yaml/parse-string)
    (catch Exception e
      ;; does not logs as user could override default logging stuff
      (println "Unable to load configuration:" path)
      (.printStackTrace e))))

(defn- init
  "Read configuration and start service."
  [path]
  (when-let [config (load-config path)]
    (set-logfile-path! (:komondor.log config))
    (start-listener config)))

(defn- usage []
  (println "Usage: java -jar komondor.jar [options]")
  (println "Options:")
  (println "    -c [file]   load configuration file"))

(defn -main [& args]
  (when-not (or (= "-c" (first args))
                (second args))
    (usage)
    (System/exit 1))
  (init (second args)))
