(ns ui.db
  (:require
   [alandipert.storage-atom :refer [local-storage]]
   [reagent.core :as reagent :refer [atom]]))

(defonce sys-config-store (local-storage
                            (atom {
                                   ;; use this for remote debugging
                                   :ws-url     "ws://localhost:18282"
                                   :api-server "http://localhost:5000"
                                   :file-root  "/mnt/internal/"
                                   })
                            :sys-config-store))

(defonce all-videos ["http://localhost:6778/pexels-low-0.mp4"
                     "http://localhost:6778/pexels-low-1.mp4"
                     "http://localhost:6778/pexels-low-2.mp4"])

(defonce views [:boxes :video-slides])

(defonce app-state (atom {:box-x 100
                          :video-id [0 1]
                          :current-view :video-slides}))

(defonce active-popup (atom nil))

(defonce rotate-deg-store (atom 0))

(defonce timeline-store (atom {:tl nil :paused? true}))
