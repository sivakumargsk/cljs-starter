(ns cljs-starter.app
  (:require [chord.client :refer [ws-ch]]
            [cljs-starter.message-list :refer [message-component]]
            [cljs.core.async :refer [chan <! >! put! close! timeout]]
            [cljs.reader :as edn]
            [clojure.string :as s]
            [reagent.core :as r]
            [sablono.core :as sab]
            [devcards.core :as dc]
            [cljs.test :as t :refer [report] :include-macros true]
            [devtools.core :as devtools])
  (:require-macros [devcards.core :as dc :refer [defcard deftest]]
                   [cljs.test :refer [is testing async]]
                   [cljs.core.async.macros :refer [go go-loop]]))

(enable-console-print!)

(defn init []
  #_(render  [:h1 "Hello, Clojurescript"]
             (.getElementById js/document "main-app-area"))
  (devtools/set-pref! :install-sanity-hints true)
  (devtools/install!)
  (devcards.core/start-devcard-ui!))


(defn receive-msgs! [!msgs server-ch]
  ;; every time we get a message from the server, add it to our list
  (go-loop []
    (let [{:keys [message error] :as msg} (<! server-ch)]
      (swap! !msgs conj (cond
                          error {:error error}
                          (nil? message) {:type :connection-closed}
                          message  message))

      (when message
        (recur)))))

(defn send-msgs! [new-msg-ch server-ch]
  ;; send all the messages to the server
  (go-loop []
    (when-let [msg (<! new-msg-ch)]
      (>! server-ch msg)
      (recur))))

(defn on-load []
  (set! (.-onload js/window)
        (fn []
          (go
            (let [{:keys [ws-channel error]}
                  (<! (ws-ch "ws://192.168.0.105:9090/ws-chat/1?name=4"
                             {:format :str }))]
              (if error
                ;; connection failed, print error
                (r/render-component
                 [:div
                  "Couldn't connect to websocket: "
                  (pr-str error)]
                 (.getElementById js/document "main-app-area"))

                (let [ ;; !msgs is a shared atom between the model (above,
                      ;; handling the WS connection) and the view
                      ;; (message-component, handling how it's rendered)
                      !msgs (doto (r/atom [])
                              (receive-msgs! ws-channel))

                      ;; new-msg-ch is the feedback loop from the view -
                      ;; any messages that the view puts on here are to
                      ;; be sent to the server
                      new-msg-ch (doto (chan)
                                   (send-msgs! ws-channel))]

                  ;; show the message component
                  (r/render-component
                   [message-component !msgs new-msg-ch]
                   (.getElementById js/document "main-app-area")))))))))

(on-load)
