(ns electron.core)

(def electron       (js/require "electron"))
(def app            (.-app electron))
(def browser-window (.-BrowserWindow electron))
(def crash-reporter (.-crashReporter electron))

(def main-window (atom nil))
(def zoom 4)

(defn init-browser []
  (let [window (browser-window.
                 (clj->js {
                           :width         1920
                           :height        1080
                           :frame          false
                           :fullscreen     true
                           :webPreferences {:nodeIntegration true}}))]
    (reset! main-window window))
  ;; Path is relative to the compiled js file (main.js in our case)
  (.loadURL ^js/electron.BrowserWindow @main-window (str "file://" js/__dirname "/public/index.html"))
  (.on ^js/electron.BrowserWindow @main-window "closed" #(reset! main-window nil)))

;; CrashReporter can just be omitted
(.start crash-reporter
        (clj->js
          {:companyName "CL"
           :productName "Chen Li"
           :submitURL   "chencassc@gmail.com"
           :autoSubmit  false}))

(.on app "window-all-closed" #(when-not (= js/process.platform "darwin")
                                (.quit app)))
(.on app "ready" init-browser)
