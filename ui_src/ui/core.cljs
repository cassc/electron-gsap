(ns ui.core
  (:require
   [ui.utils :as utils]
   [ui.views :as v]
   [ui.db :as db]
   [ui.actions :as a :refer [initer]]

   [taoensso.timbre :as t]
   [reagent.core :as r])
  (:import
   [goog.string]
   [goog.dom]))


(defn root-component []
  (fn []
    [:div
     (when-not (:started @db/app-state)
       [:div.controls
        [:button {:on-click #(a/start-video-animation!)} "Start"]])
     [v/video-slides]
     ;; [v/ball "ball1" ""]
     ;; [v/ball "ball2" ""]
     ;; [v/ball "ball3" ""]
     ;[cljsjs/anime "3.0.1-0"f; [v/boxes]
     ;; [v/star]
     ]))

(defn mount-components []
  (r/render
    [root-component]
    (js/document.getElementById "app-container")))


(enable-console-print!)

(defn ^:export main []
  @initer
  (a/reset-animation!)
  (mount-components))

(main)
