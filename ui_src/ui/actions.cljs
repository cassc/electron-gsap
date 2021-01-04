(ns ui.actions
  (:require
   [ui.db :as db]
   [ui.utils :as utils]
   [taoensso.timbre :as t]))



(defn- update-box-pos
  [{:keys [x up? rotation]}]
  (let [{:keys [w h]} (utils/window-size)
        _             (t/info "window size: " w h)
        x             (+ x 100)
        x             (if (> x w) 0 x)
        rotation      (or rotation 360)
        y             (if up? 50 (- h 100 50))]
    {:x x :y y :up? (not up?) :rotation (- rotation)}))

(defn box-next-position []
  (-> (swap! db/app-state update :box update-box-pos)
      (:box)
      (select-keys [:x :y])))

(defn- star-animation []
  (let [tl   (js/gsap.timeline (clj->js {:defaults {:duration 1}}))
        star ".star"]
    (.. tl
        ;; Left and Top are not the same thing as X and Y.
        ;;
        ;; X,Y are an alias to the translate() transform. So, if you are setting
        ;; you element at left:300, top:200, it is effectively sitting at x:0,
        ;; y:0
        (fromTo star (clj->js {:scale 0.5 :autoAlpha 0 :x 300 :y 300}) (clj->js {:autoAlpha 1 :fill :cyan :rotation (* 360 3) :scale 1}))
        (to star (clj->js {:scale 0.5 :autoAlpha 0 :rotation 360 :duration 0.01}))
        )
    tl))

(defn- ball-animation []
  (let [tl (js/gsap.timeline (clj->js {:defaults {:visibility :visible
                                                  ;; animation duration
                                                  :duration   1
                                                  :x          300
                                                  :y          300

                                                  ;; add call back for animation events
                                                  :onComplete       (fn [& args]
                                                                      (t/info "balls animation completed with params: " args))
                                                  ;; optionally pass arguments to callbacks, prints
                                                  ;; => balls animation completed with params:  ("p1" "p2")
                                                  :onCompleteParams (clj->js ["p1" "p2"])

                                                  ;; :onUpdate    #(t/info "balls animation updated")
                                                  :onStart #(t/info "balls animation started")
                                                  ;; easeIn is good for exits
                                                  ;; easeOut is good for entrance
                                                  ;; easeInOut is good for intermediate stages
                                                  :ease    "power4.easeOut"
                                                  }}))
        [b1 b2 b3]    ["#ball1" "#ball2" "#ball3"]
        {:keys [w h]} (utils/window-size)]
    (.. tl
        ;; use `set` to configure initial state
        ;; normally you'd use fromTo method instead
        ;; (set ".ball" (clj->js {:visibility :visible}))

        ;; use label to create animation for a group of objects
        (add "burst")
        (fromTo b1 (clj->js {:x 50 :y 50 :background-color :red}) (clj->js {:opacity 0.2}) "burst")
        ;; add delay when animating an element
        (fromTo b2 (clj->js {:x (- w 50) :y 50 :background-color :green}) (clj->js {:opacity 0.2}) "burst+=0.2")
        (fromTo b3 (clj->js {:x (/ w 2) :y (- h 150) :background-color :blue}) (clj->js {:opacity 0.2}) "burst"))
    tl))

(defn- boxes-animation []
  (let [tl (js/gsap.timeline)
        el ".box"]
    (.. tl
        (fromTo el (clj->js {:scale 1}) (clj->js {:scale   0.5
                                                  :y       50
                                                  :x       50
                                                  ;; Use stagger to create interesting visualization for a group of objects
                                                  ;; https://greensock.com/docs/v3/Staggers
                                                  :stagger {:each 0.1
                                                            :from "center"
                                                            :grid "auto"
                                                            :ease "power4.easeInOut"
                                                            }}))
        )
    tl))

(defn master-animation []
  (let [tl (js/gsap.timeline (clj->js {;; number of loops of animations, -1 means infinite loop
                                       :repeat      -1
                                       :onRepeat    #(t/info "master animation repeating")
                                       :repeatDelay 0.01}))]
    (.. tl
        ;; timeline can be nested
        (add (ball-animation) "balls")
        (add (star-animation) "star")
        (add (boxes-animation) "boxes")


        ;; use `seek` to go directly to specific scene
        ;; (seek "balls+=0.5")
        )

    tl))

(defn toggle-animation []
  (let [{:keys [tl paused?]} @db/timeline-store]
    (if tl
      (do
        ;; pause / resume animations
        (if paused?
          (.resume tl)
          (.pause tl))
        (swap! db/timeline-store update :paused? not))
      (let [tl (master-animation)]
        (swap! db/timeline-store assoc :tl tl :paused? false)))))

;; reset animation, for the convenience of development
(defn reset-animation! []
  (let [{:keys [tl]} @db/timeline-store]
    (when tl
      (.pause tl)
      (reset! db/timeline-store {:paused? true}))))

(defonce initer
  (delay
    (t/info "add your init scripts here")
    ))


;; Rich comment block with redefined vars ignored
#_{:clj-kondo/ignore [:redefined-var]}
(comment

  (.seek @db/timeline-store "star+=1")

  ) ;; End of rich comment block
