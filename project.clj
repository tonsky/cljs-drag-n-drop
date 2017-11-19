(defproject cljs-drag-n-drop "0.1.0"
  :description  "Sane wrapper around Drag-n-Drop DOM API"
  :license      { :name "Eclipse"
                  :url  "http://www.eclipse.org/legal/epl-v10.html" }
  :url "https://github.com/tonsky/cljs-drag-n-drop"

  :dependencies [
    [org.clojure/clojure       "1.9.0-RC1"]
    [org.clojure/clojurescript "1.9.946" :scope "provided"]
  ]

  :plugins [
    [lein-cljsbuild "1.1.7"]
    [lein-figwheel "0.5.14"]
  ]

  :profiles {
    :dev {
      :resource-paths ["dev-resources"]
    }
  }

  :figwheel {
    :server-port      8080
    :repl             false
  }

  :cljsbuild {
    :builds [
      { :id "none"
        :source-paths ["src" "dev"]
        :figwheel { :on-jsload "cljs-drag-n-drop.index/refresh" }
        :compiler {
          :main                 cljs-drag-n-drop.index
          :output-to            "dev-resources/public/index.js"
          :output-dir           "dev-resources/public/index-none"
          :asset-path           "/index-none"
          :source-map           true
          :source-map-path      "/index-none"
          :optimizations        :none
          :parallel-build       true
        } }
      { :id "advanced"
        :source-paths ["src" "dev"]
        :assert false
        :compiler {
          :main                 cljs-drag-n-drop.index
          :output-to            "dev-resources/public/index.js"
          :optimizations        :advanced
          :output-dir           "dev-resources/public/index-advanced"
          :source-map           "dev-resources/public/index.js.map"
          :source-map-path      "/public/index-advanced"
          :parallel-build       true
          :elide-asserts        true
          :closure-defines      {goog.DEBUG false}
        } }
  ]}
  :clean-targets
  ^{:protect false} [ "target"
                      "dev-resources/public/index-none"
                      "dev-resources/public/index-advanced"
                      "dev-resources/public/index.js"
                      "dev-resources/public/index.js.map" ]
  :mirrors {
    "central" {:name "central" :url "https://repo.maven.apache.org/maven2/"}
  }
)
