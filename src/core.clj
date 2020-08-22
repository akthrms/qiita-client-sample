(ns core
  (:require [clojure.edn :as edn]
            [cheshire.core :as cheshire]
            [org.httpkit.client :as http]))

(def api-url
  "https://qiita.com/api/v2/items?page=1&per_page=20&query=tag:Clojure")

(def options
  (let [{:keys [access-token]} (edn/read-string (slurp "settings.edn"))]
    {:headers {"Authorization" (str "Bearer " access-token)}}))

(defn get-items []
  (let [{:keys [body]} @(http/get api-url options)]
    (cheshire/parse-string body true)))

(defn -main []
  (doseq [{:keys [title created_at user]} (get-items)]
    (let [created-at (subs created_at 0 10)
          user-id (:id user)]
      (println (str "[" created-at "] " title " (@" user-id ")")))))
