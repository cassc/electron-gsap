(ns ui.functions
  "todo fsm functions, migrated from actions"
  (:require
   [ui.db :as db]
   [ui.utils :as utils]
   [ui.ws-client :as ws]

   [taoensso.timbre :as t]
   [clojure.core.async :refer [go go-loop timeout <! >!]]
   [clojure.string :as s :refer [split-lines]]
   [cljs-uuid-utils.core :as uuid]
   [goog.object :as go])
  (:import
   [goog.string]
   [goog.dom]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; funcs
(defn close-popup []
  (reset! db/active-popup nil))

;; document.body.setAttribute( "style", "-moz-transform: rotate(-90deg);");
(defn rotate-screen []
  (let [deg @db/rotate-deg-store
        deg (if (and deg (= 180 deg))
              0
              180)]
    (reset! db/rotate-deg-store deg)
    (utils/set-rotation! deg)))
