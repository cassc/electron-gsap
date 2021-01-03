(ns ui.db
  (:require
   [taoensso.timbre :as t]
   [alandipert.storage-atom :refer [local-storage]]
   [clojure.string :as s]
   [reagent.core :as reagent :refer [atom]]))

(defonce sys-config-store (local-storage
                            (atom {
                                   ;; use this for remote debugging
                                   :ws-url     "ws://localhost:18282"
                                   :api-server "http://localhost:5000"
                                   :file-root  "/mnt/internal/"
                                   })
                            :sys-config-store))

(defn- set-sys-config! [config]
  (swap! sys-config-store merge config))

(defonce app-state (atom {:box-x 100}))
(defonce active-popup (atom nil))
(defonce rotate-deg-store (atom 0))

(defonce timeline-store (atom {:tl nil :paused? true}))
