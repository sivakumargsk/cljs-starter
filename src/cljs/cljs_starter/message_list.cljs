(ns cljs-starter.message-list
  (:require [cljs.core.async :refer [put!]]
            [reagent.core :as r]))

;; (defn message-box [new-msg-ch]
;;   (let [!input-value (doto (r/atom nil)
;;                        (->> (set! js/window.input-value)))]
;;     (fn []
;;       [:div
;;        [:h3 "Send a message to the server:"]
;;        [:input {:type "text",
;;                 :size 50,
;;                 :autofocus true
;;                 :value @!input-value
;;                 :on-change (fn [e]
;;                              (reset! !input-value (.-value (.-target e))))

;;                 :on-key-press (fn [e]
;;                                 (when (= 13 (.-charCode e))
;;                                   (put! new-msg-ch @!input-value)
;;                                   (reset! !input-value "")))}]])))

;; (defn message-list [!msgs]
;;   [:div
;;    [:h3 "Messages from the server:"]
;;    (for [msg @!msgs]
;;      [:div (str msg)])])

;; (defn message-component [!msgs new-msg-ch]
;;   [:div
;;    [message-box new-msg-ch]
;;    [message-list !msgs]])

(defn message-box [new-msg-ch]
  (let [!input-value (doto (r/atom nil)
                       (->> (set! js/window.input-value)))]
    (fn []
      [:div
       [:div.send-wrap
        [:input.form-control.send-message
         {:placeholder "Write a reply"
          :rows "3"
          :autofocus true
          :value @!input-value
          :on-change #(reset! !input-value (-> % .-target .-value))
          :on-key-press #(when (= 13 (.-charCode %))
                           (put! new-msg-ch @!input-value)
                           (reset! !input-value ""))}]
        [:a.btn.btn-success.btn-block
         {:on-click
          #((put! new-msg-ch @!input-value)
            (reset! !input-value ""))}
         [:i.fa.fa-paper-plane]
         " Send Message"]]])))

(defn show-msg [data]
  [:div.media.msg
   [:a.pull-left
    {:href "#"}
    [:img.media-object
     {:src "https://www.internationalschoolcommunity.com/images/default_user_icon.png"
      :style {:width "50px" :height "50px"}
      :alt "64x64"}]]
   [:div.media-body
    ;; [:small.pull-right.time [:i.fa.fa-clock-o] (str " " (data :time))]
    ;; [:h5.media-heading (data :name)]
    [:small.col-lg-10 data]]])

(defn message-list [!msgs]
  (let [i (r/atom (js/Date.))]
    [:div.msg-warp
     (for [msg @!msgs]
       ^{:key (js/setInterval
               #(reset! i (js/Date.)) 1000)}
       [show-msg msg])]))

(defn message-component [!msgs new-msg-ch]
  [:div
   [:div.message-wrap.col-lg-12]
   [message-list !msgs]
   [message-box new-msg-ch]])
