(ns cljs-starter.message-list
  (:require [cljs.core.async :refer [put!]]
            [reagent.core :as r]))

(defn message-box [new-msg-ch]
  (let [!input-value (doto (r/atom nil)
                       (->> (set! js/window.input-value)))]
    (fn []
      [:div
       [:h3 "Send a message to the server:"]
       [:input {:type "text",
                :size 50,
                :autofocus true
                :value @!input-value
                :on-change (fn [e]
                             (reset! !input-value (.-value (.-target e))))

                :on-key-press (fn [e]
                                (when (= 13 (.-charCode e))
                                  (put! new-msg-ch @!input-value)
                                  (reset! !input-value "")))}]])))

(defn message-list [!msgs]
  [:div
   [:h3 "Messages from the server:"]
   (for [msg @!msgs]
     [:div (str msg)])])

(defn message-component [!msgs new-msg-ch]
  [:div
   [message-box new-msg-ch]
   [message-list !msgs]])
