(defproject electron-gsap "0.1.0-SNAPSHOT"
  :license {:name ""}
  :source-paths ["src"]
  :description "An Electron App with ClojureScript"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/clojurescript "1.10.520"]
                 [figwheel "0.5.19"]
                 [figwheel-sidecar "0.5.19"]
                 [cider/piggieback "0.4.0"]
                 [reagent "0.8.1"]
                 [ring/ring-core "1.7.1"]
                 [com.taoensso/timbre "4.10.0"]
                 [nilenso/wscljs "0.2.0"]
                 [alandipert/storage-atom "1.2.4"]
                 [com.lucasbradstreet/cljs-uuid-utils "1.0.2"]]
  :plugins [[lein-cljsbuild "1.1.7"]
            [lein-figwheel "0.5.19"]]

  :clean-targets ^{:protect false} ["resources/main.js"
                                    "resources/public/js/ui-core.js"
                                    "resources/public/js/ui-core.js.map"
                                    "resources/public/js/ui-out"]
  :cljsbuild
  {:builds
   [
    {:source-paths ["ui_src" "dev_src"]
     :id           "frontend-dev"
     :compiler     {:output-to      "resources/public/js/ui-core.js"
                    :output-dir     "resources/public/js/ui-out"
                    :source-map     true
                    :asset-path     "js/ui-out"
                    :optimizations  :none
                    :cache-analysis true
                    :main           "dev.core"}}
    {:source-paths ["electron_src"]
     :id           "electron-dev"
     :compiler     {:output-to      "resources/main.js"
                    :output-dir     "resources/public/js/electron-dev"
                    :optimizations  :simple
                    :pretty-print   true
                    :cache-analysis true}}
    {:source-paths ["electron_src"]
     :id           "electron-release"
     :compiler     {:output-to      "resources/main.js"
                    :output-dir     "resources/public/js/electron-release"
                    :externs        ["cljs-externs/common.js"]
                    :optimizations  :simple
                    :cache-analysis true
                    :infer-externs  true}}
    {:source-paths ["ui_src"]
     :id           "frontend-release"
     :compiler     {:output-to      "resources/public/js/ui-core.js"
                    :output-dir     "resources/public/js/ui-release-out"
                    :source-map     "resources/public/js/ui-core.js.map"
                    :externs        ["cljs-externs/common.js"]
                    :optimizations  :simple
                    :cache-analysis true
                    :infer-externs  true
                    :process-shim   false
                    :main           "ui.core"}}]}
  :profiles {:dev {:dependencies [[cider/piggieback "0.4.0"]
                                  [org.clojure/tools.nrepl "0.2.13"]]
                   :repl-options {:nrepl-middleware [cider.piggieback/wrap-cljs-repl]}}}
  :figwheel {:http-server-root  "public"
             :css-dirs          ["resources/public/css"]
             :ring-handler      tools.figwheel-middleware/app
             :open-file-command "emacsclient"
             :server-port       3449})
