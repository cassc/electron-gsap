(ns ui.ws-client
  (:require
   [ui.db :as db]
   [cljs.core.async :refer [go-loop <!]]
   [taoensso.timbre :as t]
   [wscljs.client :as ws]
   [wscljs.format :as fmt]
   [clojure.string :as s]
   [clojure.core.async :as a]))

(defonce socket-store (atom nil))

(defn ->json [ss]
  (js->clj (js/JSON.parse ss) :keywordize-keys true))

(defn handle-ws-msg [msg]
  (when-not (s/blank? msg)
    (let [{:keys [tpe target data] :as params} (->json msg)]
      (t/info "write your websocket message logic here"))))

(def handlers {:on-message (fn [e]
                             (try
                               (handle-ws-msg (.-data e))
                               (catch :default err
                                 (t/error "handle-ws-msg" err))
                               (finally
                                 ;; (t/info "recv:" (.-data e))
                                 )))
               :on-open    #(do
                              (t/info "Opening a new connection")
                              (swap! socket-store assoc :open? true))
               :on-close   #(do
                              (prn "Closing a connection")
                              (reset! socket-store nil))})

(defn recreate-socket! []
  (t/info "creating ws socket ...")
  (let [socket (ws/create
                (:ws-url @db/sys-config-store)
                handlers)]
    (reset! socket-store {:socket socket
                          :open? nil})))

(defn get-socket []
  (let [{:keys [open? socket]} (or @socket-store
                                   (recreate-socket!))]
    (when open?
      socket)))

(defn send! [msg]
  (t/info "sending" msg)
  (if-let [sock (get-socket)]
    (ws/send sock msg fmt/json)
    (t/warn "WS socket not ready, not sending msg:" msg)))

(defn keep-alive! []
  (go-loop []
    (try
      (get-socket)
      (catch :default e
        (t/error e)))
    (<! (a/timeout 3000))
    (recur)))
