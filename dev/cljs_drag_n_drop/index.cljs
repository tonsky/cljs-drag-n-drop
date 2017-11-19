(ns cljs-drag-n-drop.index
  (:require
    [cljs-drag-n-drop.core :as dnd]))


(enable-console-print!)


(defn q [selector] 
  (js/document.querySelector selector))


(defn ^:export refresh []
  (let [outer  (q ".outer")
        center (q ".center")
        inner  (q ".inner")]
    (dnd/subscribe! center :d1
      { :timeout-ms 200
        :start (fn [e] (println "d1 start"))
        :enter (fn [e] (println "d1 enter") (.add (.-classList center) "over"))
        :drop  (fn [e files] (println "d1 drop") (js/console.log files))
        :leave (fn [e] (println "d1 leave") (.remove (.-classList center) "over"))
        :end   (fn [e] (println "d1 end")) })

    (dnd/subscribe! center :d2
      { :start (fn [e] (println "d2 start"))
        :enter (fn [e] (println "d2 enter") (.add (.-classList center) "over"))
        :drop  (fn [e files] (println "d2 drop") (js/console.log files))
        :leave (fn [e] (println "d2 leave") (.remove (.-classList center) "over"))
        :end   (fn [e] (println "d2 end")) })

    (dnd/subscribe! js/document.documentElement :html
      { :start (fn [e] (println "HTML start"))
        :enter (fn [e] (println "HTML enter"))
        :drop  (fn [e files] (println "HTML drop") (js/console.log files))
        :leave (fn [e] (println "HTML leave"))
        :end   (fn [e] (println "HTML end")) })))