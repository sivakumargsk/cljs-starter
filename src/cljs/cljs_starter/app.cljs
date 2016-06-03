(ns cljs-starter.app
  (:require [reagent.core :as reagent :refer [atom render]]
            [sablono.core :as sab]
            [devcards.core :as dc]
            [goog.date.Date]
            [cljs.test :as t :refer [report] :include-macros true]
            [cljs-time.core    :refer [now days minus day-of-week]]
            [cljs-time.format  :refer [formatter unparse]]
            [re-com.core
             :refer [h-box v-box box
                     gap line
                     button info-button
                     label input-text
                     checkbox
                     throbber hyperlink-href p
                     single-dropdown
                     datepicker
                     datepicker-dropdown]
             :refer-macros [handler-fn]]
            )
  (:require-macros [devcards.core :as dc :refer [defcard deftest]]
                   [cljs.test :refer [is testing async]]))

(enable-console-print!)

(defn init []
  #_(render  [:h1 "Hello, Clojurescript"]
             (.getElementById js/document "main-app-area"))
  (devcards.core/start-devcard-ui!))


;; ---------------------------------------------------------------------

(defn microsoft-button [hover?]
  [button
   :label [:span "Microsoft Modern Button "
           [:i.zmdi.zmdi-hc-fw-rc.zmdi-download]]
   :on-click #()
   :style {:color            "white"
           :background-color (if @hover? "#0072bb" "#4d90fe")
           :font-size        "22px"
           :font-weight      "300"
           :border           "none"
           :border-radius    "0px"
           :padding          "20px 26px"}
   :attr {:on-mouse-over (handler-fn (reset! hover? true))
          :on-mouse-out  (handler-fn (reset! hover? false))}] )

(defn bootstrap-button [state]
  [:div.row
   [:div.col-md-4
    [button
     :label (if (:see-throbber @state)  "Stop it!" "See Throbber")
     :tooltip "I'm a tooltip on the bottom"
     ;; :tooltip-position :left-center
     ;; by default bottom-center
     :on-click #(swap! state update-in [:see-throbber] not)]]
   [:div.col-md-2
    (when (:see-throbber @state) [throbber])]])

(defn re-com-buttons []
  (let [hover? (atom false)
        state (atom {:see-throbber false})]
    (fn []
      [:div.container
       [:div.form-group
        [:div.row
         [microsoft-button hover?]]]
       [:div.form-group
        [bootstrap-button state]]])))

(defcard Re-Com-Buttons
  (reagent/as-element [re-com-buttons]))


;; -------------------------------------------------------------------
;; Info Buttons

(defn info-msg []
  [v-box
   :children [[:p.info-heading "Info Popup Heading"]
              [:p "You can use the " [:span.info-bold "info-bold"] " class to make text bold."]
              [:p "Use the " [:span.info-bold "code"] " element to display source code:"]
              [:code
               "(defn square [n] (* n n))" [:br]
               "=> #'user/square" [:br]
               "(square 45)" [:br]
               "=> 2025" [:br]
               ]
              [:p.info-subheading "Sub heading"]
              [:p
               "Note: Styles copied from "
               [hyperlink-href
                :label  "ClojureScript Cheatsheet"
                :href   "http://cljs.info/cheatsheet"
                :target "_blank"]
               "."]]])

(defn infobutton []
  (let [info (info-msg)]
    (fn []
      [:div.form-group
       [:div
        [:span.field-label "client"]
        [info-button :info info]]
       [input-text
        :model       ""
        :placeholder "Example input #1"
        :on-change   #()]])))


(defcard Re-Com-InfoButtons
  (reagent/as-element [infobutton]))


;; ---------------------------------------------------------------------
;; checkboxes

(defn re-checkbox []
  (let [ischecked (atom {:apple false
                         :boy false})]
    (fn []
      [:div.form-group
       [:div.row
        [:div.col-md-6
         [checkbox
          :label "Apple "
          :model (@ischecked :apple)
          :on-change #(swap! ischecked assoc :apple %)]
         [checkbox
          :label "Boy "
          :model (@ischecked :boy)
          :on-change #(swap! ischecked assoc :boy %)]]
        [:div.col-md-6
         [:button.btn.btn-info {:on-click #(reset! ischecked {})} "clear"]]]
       [:p (str @ischecked)]])))

(defcard re-com-checkbox
  (reagent/as-element [re-checkbox]))


;; ---------------------------------------------------------------------
;; throbber

(defn my-throbber []
  [:div.row
   [:div.col-md-4 [throbber
                   :size  :small
                   :color "red"]]
   [:div.col-md-4 [throbber]]
   [:div.col-md-4 [throbber
                   :size  :large
                   :color "blue"]]])

(defcard recom-throbber
  (reagent/as-element [my-throbber]))

;; ----------------------------------------------------------------------
;; select list

(def countries [{:id "au" :label "Australia"}
                {:id "us" :label "United States"}
                {:id "uk" :label "United Kingdom"}
                {:id "ca" :label "Canada"}
                {:id "nz" :label "New Zealand"}])

(defn myselect []
  (let [model-data (atom nil )]
    (fn []
      [:div
       [:div.row
        [:div.col-md-6
         [:input.form-control {:type "text"}]]
        [:div.col-md-6
         [:input.form-control {:type "text"}]]]
       [:div.row
        [:div.col-md-6
         [single-dropdown
          :choices     countries
          :model       model-data
          :placeholder "Choose a country"
          :width       "400px"
          :max-height  "400px"
          :filter-box? false
          :on-change   #()
          ]]
        [:div.col-md-6
         [single-dropdown
          :choices     countries
          :model       model-data
          :placeholder "Choose a country"
          :width       "400px"
          :max-height  "400px"
          :filter-box? false
          :on-change   #()
          ]]]])))

(defcard recom-single-select
  (reagent/as-element [myselect]))

;; ----------------------------------------------------------
;; Date picker
