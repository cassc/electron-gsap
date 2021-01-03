(ns ui.views
  "fsm views
  Should not call `ui.functions` unless really necessary, e.g., in `r/create-class`"
  (:require
   [ui.actions :as a]
   [ui.db :as db]
   [ui.utils :as utils]
   [ui.http :as http]

   [reagent.core :as r]
   [taoensso.timbre :as t]
   [clojure.core.async :refer [go go-loop timeout <! >!]]
   [clojure.string :as s :refer [split-lines]]
   [cljs-uuid-utils.core :as uuid]
   [goog.object :as go])
  (:import
   [goog.string]
   [goog.dom]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; views
(defn ball [id label]
  [:div.ball.hidden {:id id} label])

(defn star []
  [:div.star.hidden
   [:svg
    {:xmlns "http://www.w3.org/2000/svg", :preserveAspectRatio "xMidYMid meet" :x 0 :y 0 :view-box "0 0 86.838 86.838"}
    [:path {:d "M77.327 77.327l-28.17-9.553-23.84 17.79.38-29.743L1.413 38.646l28.405-8.83 8.83-28.404L55.82 25.698l29.744-.38-17.79 23.839z", :fill "none", :stroke "#ff0069", :stroke-width "1.265"}]]])
