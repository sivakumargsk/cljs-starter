(ns cljs-starter.app
  (:require [reagent.core :as reagent :refer [atom render]]
            [sablono.core :as sab]
            [devcards.core :as dc]
            [cljs.test :as t :refer [report] :include-macros true]
            [devtools.core :as devtools]
            [cljsjs.react-bootstrap])
  (:require-macros [devcards.core :as dc :refer [defcard deftest]]
                   [cljs.test :refer [is testing async]]))

(enable-console-print!)

(defn init []
  #_(render  [:h1 "Hello, Clojurescript"]
             (.getElementById js/document "main-app-area"))
  (devtools/set-pref! :install-sanity-hints true)
  (devtools/install!)
  (devcards.core/start-devcard-ui!))

(def list-group (reagent/adapt-react-class (aget js/ReactBootstrap "ListGroup")))
(def list-group-item (reagent/adapt-react-class (aget js/ReactBootstrap "ListGroupItem")))

(def pager (reagent/adapt-react-class (aget js/ReactBootstrap "Pager")))
(def page-item (reagent/adapt-react-class (aget js/ReactBootstrap "PageItem")))


(defn simple-component []
  [:div.container
  [:div.row
   [:div.col-md-7
    [:div.row
     [:video {:id "video" :controls true :width "650"  :poster "http://localhost:8000/part2.jpg" }
      [:source  {:src "http://localhost:8000/part1.mp4" :type "video/mp4"}]]]
    [pager
     [page-item {:href ""} "Previous"]
     [page-item {:href ""} "Next"]]]
   [:div.col-md-5 {:style {:overflow "scroll" :height "700px"}}
     [list-group
      [list-group-item {:href "#"}
       [:div.row
        [:div.col-md-4
         [:img.img-rounded
          {:src "http://localhost:8000/part1.jpg" :alt "Cinque Terre"
           :width "100" :height "75" :align "left"}]]
        [:div.col-md-8
         [:span [:b [:u "Part-1 Tutorials"]]
          "How Know the Basics of Bussiness circle"]]]]
      [list-group-item {:href "#"}
       [:div.row
        [:div.col-md-4
         [:img.img-rounded
          {:src "http://localhost:8000/part2.jpg" :alt "Cinque Terre"
           :width "100" :height "75" :align "left"}]]
        [:div.col-md-8
         [:span [:b [:u "Part-2 Tutorials"]]
          "How Know the Basics of Bussiness circle"]]]]
      [list-group-item {:href "#"}
       [:div.row
        [:div.col-md-4
         [:img.img-rounded
          {:src "http://localhost:8000/part3.jpg" :alt "Cinque Terre"
           :width "100" :height "75" :align "left"}]]
        [:div.col-md-8
         [:span [:b [:u "Part-3 Tutorials"]]
          "How Know the Basics of Bussiness circle"]]]]
      [list-group-item {:href "#"}
       [:div.row
        [:div.col-md-4
         [:img.img-rounded
          {:src "http://localhost:8000/part4.jpg" :alt "Cinque Terre"
           :width "100" :height "75" :align "left"}]]
        [:div.col-md-8
         [:span [:b [:u "Part-4 Tutorials"]]
          "How Know the Basics of Bussiness circle"]]]]
      [list-group-item {:href "#"}
       [:div.row
        [:div.col-md-4
         [:img.img-rounded
          {:src "http://localhost:8000/part2.jpg" :alt "Cinque Terre"
           :width "100" :height "75" :align "left"}]]
        [:div.col-md-8
         [:span [:b [:u "Part1 Tutorials"]]
          "How Know the Basics of Bussiness circle"]]]]
      [list-group-item {:href "#"}
       [:div.row
        [:div.col-md-4
         [:img.img-rounded
          {:src "http://localhost:8000/part3.jpg" :alt "Cinque Terre"
           :width "100" :height "75" :align "left"}]]
        [:div.col-md-8
         [:span [:b [:u "Part1 Tutorials"]]
          "How Know the Basics of Bussiness circle"]]]]
      [list-group-item {:href "#"}
       [:div.row
        [:div.col-md-4
         [:img.img-rounded
          {:src "http://localhost:8000/part5.jpg" :alt "Cinque Terre"
           :width "100" :height "75" :align "left"}]]
        [:div.col-md-8
         [:span [:b [:u "Part1 Tutorials"]]
          "How Know the Basics of Bussiness circle"]]]]
      [list-group-item {:href "#"}
       [:div.row
        [:div.col-md-4
         [:img.img-rounded
          {:src "http://localhost:8000/part4.jpg" :alt "Cinque Terre"
           :width "100" :height "75" :align "left"}]]
        [:div.col-md-8
         [:span [:b [:u "Part1 Tutorials"]]
          "How Know the Basics of Bussiness circle"]]]]
      [list-group-item {:href "#"}
       [:div.row
        [:div.col-md-4
         [:img.img-rounded
          {:src "http://localhost:8000/part5.jpg" :alt "Cinque Terre"
           :width "100" :height "75" :align "left"}]]
        [:div.col-md-8
         [:span [:b [:u "Part1 Tutorials"]]
          "How Know the Basics of Bussiness circle"]]]]]]]])

;; (defcard simple-component
;;   (reagent/as-element [simple-component]))

(render  [simple-component]
         (.getElementById js/document "main-app-area"))
