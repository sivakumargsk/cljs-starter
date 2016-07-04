(ns cljs-starter.app
  (:require [reagent.core :as reagent :refer [atom render]]
            [sablono.core :as sab]
            [devcards.core :as dc]
            [cljs.test :as t :refer [report] :include-macros true]
            [devtools.core :as devtools])
  (:require-macros [devcards.core :as dc :refer [defcard deftest]]
                   [cljs.test :refer [is testing async]]))

(enable-console-print!)

(defn init []
  #_(render  [:h1 "Hello, Clojurescript"]
             (.getElementById js/document "main-app-area"))
  (devtools/set-pref! :install-sanity-hints true)
  (devtools/install!)
  (devcards.core/start-devcard-ui!))


(defn progresSim [all ctx cw ch]
  (let [diff (.toFixed (* (/ @all 100) Math/PI 2 10) 2)]
    (.clearRect ctx 0 0 cw ch)
    (set! (.-lineWidth ctx) 25)
    (set! (.-fillStyle ctx) "#09F")
    (set! (.-strokeStyle ctx) "#09F")
    (set! (.-textAlign ctx) "center")
    (.fillText ctx (str @all "%") (* cw 0.5) (+ (* ch 0.5) 2) cw)
    (.beginPath ctx)
    (.arc ctx 70 70 45 4.72 (+ 4.72 (/ diff 10)) false)
    (.stroke ctx)
    (if (>= @all 100)
      (js/clearTimeout (js/setInterval (fn [] (progresSim all ctx cw ch)) 50))
      (swap! all inc))))

(defn my-component []
  (reagent/create-class
   {:component-did-mount
    #(let [all (reagent/atom 0)
           ctx (.getContext
                (.getElementById js/document "mycanvas")
                "2d")
           cw (.-width (.-canvas ctx))
           ch (.-height (.-canvas ctx))]
       (js/setInterval (fn [] (progresSim all ctx cw ch)) 50))
    :reagent-render
    (fn []
      [:div [:canvas {:id "mycanvas"
                      :width 150
                      :height 150
                      :style {:border "1px dashed #ccc"}}]])}))



(defcard canvas-cicular-progress
  (reagent/as-element [my-component]))
