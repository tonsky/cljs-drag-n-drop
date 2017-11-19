(ns cljs-drag-n-drop.core
  (:require
    [clojure.string :as str]
    [goog.object :as gobj]))


(defn- kill-timer! [*timer]
  (when-some [t @*timer]
    (js/clearTimeout t)
    (reset! *timer nil)))


(defn- dom-key [key]
  (str "cljs-drag-n-drop." key))


(defn- noop ([_]) ([_ _]))


(defn subscribe! [el key opts]
  (let [opts        (merge { :timeout-ms 500 :start noop :enter noop :drop noop :leave noop :end noop } opts)
        *timer      (atom nil)
        *ignore?    (atom nil)
        *enters     (atom 0)
        dragstart   (fn [_] (reset! *ignore? true))
        dragend     (fn [_] (reset! *ignore? false))
        end!        (fn [e]
                      (kill-timer! *timer)
                      (when (pos? @*enters)
                        (reset! *enters 0)
                        ((:leave opts) e))
                      ((:end opts) e))
        dragover    (fn [e]
                      (when-not @*ignore?
                        (.preventDefault e)
                        (when (and (== 0 @*enters) (nil? @*timer))
                          (js/setTimeout #((:start opts) e) 0))
                        (kill-timer! *timer)
                        (reset! *timer
                          (js/setTimeout
                            (fn []
                              (reset! *timer nil)
                              (end! e))
                            (:timeout-ms opts)))))
        drop        (fn [e]
                      (when-not @*ignore?
                        (.preventDefault e)
                        (let [files (-> e (gobj/get "dataTransfer") (gobj/get "files"))]
                          ((:drop opts) e files))
                        (end! e)))
        dragenter   (fn [e]
                      (when (nil? @*timer)
                        (js/setTimeout #((:start opts) e) 0))
                      (when (== 0 @*enters)
                        ((:enter opts) e))
                      (swap! *enters inc))
        dragleave   (fn [e]
                      (swap! *enters #(max 0 (dec %)))
                      (when (== 0 @*enters)
                        ((:leave opts) e)))]
    (js/document.documentElement.addEventListener "dragstart" dragstart)
    (js/document.documentElement.addEventListener "drag" dragstart)
    (js/document.documentElement.addEventListener "dragend" dragend)
    (js/document.documentElement.addEventListener "dragover" dragover)
    (.addEventListener el "drop" drop)
    (.addEventListener el "dragenter" dragenter)
    (.addEventListener el "dragleave" dragleave)
    (gobj/set el (dom-key key)
      { :timer     *timer
        :ignore?   *ignore?
        :enters    *enters
        :dragstart dragstart
        :dragend   dragend
        :end       end!
        :dragover  dragover
        :drop      drop
        :dragenter dragenter
        :dragleave dragleave })))


(defn unsubscribe! [el key]
  (let [s (gobj/get el (dom-key key))]
    (gobj/remove el (dom-key key))
    (when (some? @(:timer s))
      ((:end s) nil))
    (js/document.documentElement.removeEventListener "dragstart" (:dragstart s))
    (js/document.documentElement.removeEventListener "drag"      (:dragstart s))
    (js/document.documentElement.removeEventListener "dragend"   (:dragend s))
    (js/document.documentElement.removeEventListener "dragover"  (:dragover s))
    (.removeEventListener el "drop"      (:drop s))
    (.removeEventListener el "dragenter" (:dragenter s))
    (.removeEventListener el "dragleave" (:dragleave s))))

