(defproject komondor "0.1.0-SNAPSHOT"
  :description "Forward Rancher monitoring data to Riemann."
  :url "http://github.com/sanel/komondor"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clj-http-lite "0.3.0"]
                 [org.clojure/data.json "0.2.6"]
                 [stylefruits/gniazdo "1.0.0"]
                 [clj-yaml "0.4.0"]
                 [org.clojure/tools.logging "0.4.0"]
                 [org.slf4j/slf4j-api "1.7.5"]
                 [org.slf4j/slf4j-log4j12 "1.7.5"]]
  :repl-options {:port 7888}
  :profiles {:uberjar
             {:aot :all
              :uberjar-name "komondor.jar"}}
  :global-vars {*warn-on-reflection* true}
  :omit-source true
  :main komondor.core)
