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
    [:div.background
     [:button {:on-click #(a/toggle-animation)} (if (:paused? @db/timeline-store)
                                                  "Start animation"
                                                  "Pause animation")]
     [v/ball "ball1" ""]
     [v/ball "ball2" ""]
     [v/ball "ball3" ""]

     [v/star]
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
