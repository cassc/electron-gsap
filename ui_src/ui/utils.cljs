(ns ui.utils
  (:require
   [taoensso.timbre :as t]
   [clojure.string :as s]))

;; parse normal filename for meta info
(defn parse-filename [title]
  (let [ext (some-> (s/split title #"\.") last s/lower-case s/trim)]
    (cond
      (#{"jpg" "png" "svg" "jpeg" "gif"} ext) {:tpe :image :icon-class "far fa-image"}
      (#{"mp3" "wav"} ext)                    {:tpe :music :icon-class "fas fa-music"}
      (#{"txt" "log"} ext)                    {:tpe :text :icon-class "mdi mdi-code-braces"}
      (#{"py" "c" "c++" "cpp"} ext)           {:tpe :program :icon-class "mdi mdi-code-braces"}
      :else                                   {:tpe :unknown :icon-class "fas fa-question"})))

(defn now []
  (.getTime (js/Date.)))

(defn set-rotation! [deg]
  (t/info "transform" deg)
  (set! (.-transform js/document.body.style) (str "rotate( "deg  "deg)")))


(defn add-padding [width s]
  (let [s (str s)
        padding (max 0 (- width (count s)))]
    (apply str (s/join (repeat padding "0")) s)))

(defn secs-to-hhmm [secs]
  (str (add-padding 2 (int (/ secs 60)))
       ":"
       (add-padding 2 (int (rem secs 60)))))

(defn clamp [i n-min n-max]
  (if i
    (cond
      (> i n-max) n-max
      (< i n-min) n-min
      :else       i)
    n-min))


(defn window-size []
  {:w (.-innerWidth js/window)
   :h (.-innerHeight js/window)})
