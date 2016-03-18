(ns cljs-starter.app
  (:require [reagent.core :as reagent :refer [atom render]]
            [sablono.core :as sab]
            [devcards.core :as dc]
            [cljs.test :as t :refer [report] :include-macros true]
            [devtools.core :as devtools]
            ;;[reforms.reagent :include-macros true :as f]
            [bouncer.core :as b]
            [bouncer.validators :as v]
            )
  (:require-macros [devcards.core :as dc :refer [defcard deftest]]
                   [cljs.test :refer [is testing async]]))

(enable-console-print!)

(defn init []
  #_(render  [:h1 "Hello, Clojurescript"]
             (.getElementById js/document "main-app-area"))
  (devtools/set-pref! :install-sanity-hints true)
  (devtools/install!)
  (devcards.core/start-devcard-ui!))

;; ==================================================================================
;; Reagent: Minimalistic React for ClojureScript
;; http://reagent-project.github.io/
;; ==================================================================================

;;A very basic Reagent component may look something like this:
(defn simple-component []
  [:div
   [:p "I am a component!"]
   [:p.someclass
    "I have " [:strong "bold"]
    [:span {:style {:color "red"}} " and red "] "text."]])

(defcard simple-component
  (reagent/as-element [simple-component]))

;; ;;rendering a component in reagent with out defcards

;; (defn render-simple []
;;   (reagent/render-component [simple-component]
;;                             (.-body js/document)))
;; (render-simple)


;;You can build new components using other components as building blocks. Like this:
(defn simple-parent []
  [:div
   [:p "I include simple-component"]
   [simple-component]])

(defcard simple-parent
  (reagent/as-element [simple-parent]))


;;Data is passed to child components using plain old Clojure data types. Like this:
(defn hello-component [name]
  [:p "Hello, " name "!"])

(defn say-hello []
  [hello-component "world"])

(defcard say-hello-world
  (reagent/as-element [say-hello]))


;;myexample
(defn myspan [col text]
  [:span {:style {:color col}} text])

(defn hello-clojure []
  [:h1
   [:strong   "Welcome to "
    [myspan "green" "Clojure"] [myspan "#003388" " Script"]]])

(defcard greetings
  (reagent/as-element [hello-clojure]))



;;Here is another example that shows items in a seq:
(defn lister [items]
  [:ul
   (for [item items]
     [:li "item" item])])

(defn lister1 [items]
  [:ul
   (for [item items]
     ^{:key item} ;;it helps to return unique items if items are rtepeated in list
     ;;attaching a unique key to every item in a dynamically
     ;;generated list of components is good practice,
     ;;and helps React to improve performance for large lists.
     [:li "item" item])])

(defn lister-user []
  [:div
   "Here is a list:"
   [lister1 `(1 1 2 2 3 3 6 6)]])

(defcard user-list
  (reagent/as-element [lister-user]))


;;Managing state in Reagent with help reagent Atom
(def click-count (reagent/atom 0))

(defn counting-component []
  [:div
   "The atom " [:code "click-count"]  " has value:"
   @click-count "."[:br]
   [:input {:type "button" :value "click me!"
            :on-click #(swap! click-count inc)}]])

(defcard counting-clicks
  (reagent/as-element [counting-component]))

;;Sometimes you may want to maintain state locally in a component. That is easy to do with an atom.
(defn timer-component []
  (let [seconds-elapsed (reagent/atom 0)]
    (fn []
      (js/setTimeout #(swap! seconds-elapsed inc) 1000)
      [:div
       "Seconds Elapsed: " @seconds-elapsed])))

(defcard timer
  (reagent/as-element [timer-component]))

;;reagent Atom allows you to perform some setup of newly created components without
;;resorting to React’s lifecycle events.
;; By simply passing an atom around you can share state management between components, like this:
(defn my-atom-input [val]
  [:input {:type "text"
           :value @val
           :on-change #(reset! val (.-value (.-target %)))}])

(defn shared-state []
  (let [value (reagent/atom "foo")]
    (fn []
      [:div
       [:p "The value is now:" @value]
       [:p "Change it here: " [my-atom-input value]]])))

(defcard state-management
  (reagent/as-element [shared-state]))

;;=================================================================================
;;;Simple form
(defn in-text [id-name]
  [:input#id-name {:type "text"}])

(defcard in-type
  (reagent/as-element [in-text "sample" ]))

(defn label [name]
  [:div
   [:label  name [in-text name]]])

(defcard label
  (reagent/as-element [label "first Name" ]))

(defn form []
  [:div
   [label "First Name"]
   [label "Second Name"]
   [label "Place"]
   [label "phone No"]])

(defcard myForm
  (reagent/as-element [form]))

;; ===================================================================================
;; Form Creation in clojureScript
;; ===================================================================================

(def mydata (reagent/atom {:docName  "DocumentName"
                           :title    "title"
                           :empName  "Emp Name"
                           :date     "2014-02-01"
                           :location "india"
                           :isactive "false"}))

;;i create textbox using closure concept because textbox placeholder is changed every time
(defn input-text [id]
  (fn [textbox-name]
    [:input#id.form-control {:type "text"
                             :placeholder (str "Enter "textbox-name)
                             :on-change #(swap! mydata assoc id (-> % .-target .-value) )}]))


;;i create label using closure concept because label name is changed every time
(defn mylabel [id]
  (fn [labelname]
    [:label#id.control-label labelname]))

(defn myblock [id labelname]
  [:div#id.form-group
   [(mylabel id) labelname]
   [(input-text id) labelname]])

(defn mybutton [id fun]
  [:div.form-group.form-buttons
   [:button#id.btn.btn-primary {:type "button"
                                :onClick fun }
    "Submit"] "\t"
   [:button.btn.btn-primary {:type "reset"}
    "Reset"]])

(defn myform [id]
  [:form.id
   [myblock :docName  "Document Name" ]
   [myblock :title    "Titile Name"]
   [myblock :empName  "Employee Name"]
   [myblock :date     "Date (yyyy-mm-dd)"]
   [myblock :location "Location"]
   [myblock :isactive "Is Active"]
   [mybutton "submit-1" #(js/alert @mydata)]])

(defcard Simple-Form
  (reagent/as-element [myform "RRMS Form"]))

;; ========================================================================================
;; login form with validations
;; (Building a login form in Clojurescript and Reagent)
;; https://dhruvp.github.io/2015/02/23/mailchimip-clojure/
;; ========================================================================================
;; ---------------------------------------------------------------------------
;; genric functions

;; input-element
(defn input-element
  "An input element which updates its value on change"
  [id name type value in-focus]
  [:input#id.form-control {:name name
                           :type type
                           :required ""
                           :value @value
                           :on-change #(reset! value (-> % .-target .-value))
                           ;; Below we change the state of in-focus
                           :on-focus #(swap! in-focus not)
                           :on-blur #(swap! in-focus not)
                           }])

(defn input-and-prompt
  "Creates an input box and a prompt box that appears above the input comes into focus."
  [label-value input-name input-type input-element-arg prompt-element required?]
  (let [input-focus (reagent/atom false)]
    (fn []
      [:div
       [:label label-value]
       (if @input-focus
         prompt-element
         [:div])
       [input-element input-name input-name input-type input-element-arg input-focus]
       (if (and required? (= "" @input-element-arg))
         [:div [:code  "Field is required!"]]
         [:div])])))

;; prompt-message
(defn prompt-message
  "A prompt that will animate to help the user with a given input"
  [message]
  [:div.my-message
   [:div.prompt.message-animation [:p message]]])


;; Finally, notice that password-form, input-form etc.
;; aren’t in a form-group class. Let’s fix that. We create the following function:
(defn wrap-as-element-in-form
  [element]
  [:div.row.form-group  element])


;;-------------------------------------------------------------------------------
;;Applying Additional validation on the password

(defn check-nil-then-predicate
  "Check if the value is nil, then apply the predicate"
  [value predicate]
  (if (nil? value)
    false
    (predicate value)))

(defn eight-or-more-characters?
  [word]
  (check-nil-then-predicate word (fn [arg] (> count arg) 7)))

(defn has-special-character?
  [word]
  (check-nil-then-predicate word (fn [arg]
                                   (boolean (first (re-seq #"\w+" arg))))))

(defn has-number?
  [word]
  (check-nil-then-predicate word (fn [arg]
                                   (boolean (re-seq #"\d+" arg)))))

(defn password-requirements
  "A list to describe which password requirements have been met so far"
  [password requirements]
  [:div
   [:ul (->> requirements
             (filter (fn [req] (not ((:check-fn req) @password))))
             (doall)
             (map (fn [req] ^{:key req} [:li (:message req)])))]])

;; -----------------------------------------------------------------------------
;; declared input-elements
(defn email-form  [email-atom]
  (input-and-prompt  "email"
                     "email"
                     "email"
                     email-atom
                     (prompt-message "What's your email?")
                     true))

(defn name-form  [name-atom]
  (input-and-prompt  "name"
                     "name"
                     "text"
                     name-atom
                     (prompt-message "What's your name?")
                     true))

(defn password-form
  [password]
  (let [password-type-atom (reagent/atom "password")]
    (fn []
      [:div
       [(input-and-prompt  "password"
                           "password"
                           @password-type-atom
                           password
                           (prompt-message "What's your password?")
                           true)]
       [password-requirements
        password [{:message "8 or more characters" :check-fn eight-or-more-characters?}
                  {:message "At least one special character" :check-fn has-special-character?}
                  {:message "At least one number" :check-fn has-number?}]]])))



;; ------------------------------------------------------------------------------
;; views pages

(defn home-page []
  ;; we define a email-address as an atom right here
  (let [email-address (reagent/atom nil)
        name (reagent/atom nil)
        password (reagent/atom nil)]
    (fn []
      [:div
       [:div.signup-form
        [:h2 "Simple login-form"]
        [:form
         ;;we the email-input component here
         (wrap-as-element-in-form [email-form email-address] )
         (wrap-as-element-in-form [name-form name])
         (wrap-as-element-in-form [password-form password])]]])))

;; ------------------------------------------------------------------------------
;; Rendering

(defcard log-in-validation
  (reagent/as-element [home-page]))


;; ==========================================================================================
;; simple form through this link
;; http://yogthos.net/posts/2014-07-15-Building-Single-Page-Apps-with-Reagent.html
;; ==========================================================================================

;; ------------------------------------------------------------------
;; state management

(def state (atom {:doc {} :saved? false}))

(defn set-value! [id value]
  (swap! state assoc :saved? false)
  (swap! state assoc-in [:doc id] value))

(defn get-value [id]
  (get-in @state [:doc id]))

;; ------------------------------------------------------------------
;; elements

(defn row [label & body]
  [:div.row
   [:div.col-md-2 [:label label]]
   [:div.col-md-3 body]])

(defn text-input [id label]
  [row label
   [:input.form-control {:type "text"
                         :value (get-value id)
                         :on-change #(set-value! id (-> % .-target .-value))}]])


(defn list-item [id k v selections]
  (letfn [(handle-click! []
            (swap! selections update-in [k] not)
            (set-value! id (->> @selections
                                (filter second)
                                (map first))))]
    [:li {:class (str "list-group-item"
                      (if (k @selections)  " active"))
          :on-click handle-click!}
     v]))

(defn selection-list [id label & items]
  (let [selections (->> items (map (fn [[k]] [k false])) (into {}) atom)]
    (fn []
      [:div.row
       [:div.col-md-2 [:span label]]
       [:div.col-md-5
        [:div.row
         (for [[k v] items]
           [list-item id k v selections])]]])))

;; ---------------------------------------------------------------------------------
;; pages

(defn home []
  [:div
   [:div.page-header [:h1 "Reagent Form"]]
   [text-input :first-name "First name"]
   [text-input :last-name "Last Name"]

   [selection-list :favorite-drinks "Favorite drinks"
    [:coffee "Coffee"]
    [:beer "Beer"]
    [:crab-juice "Crab juice"]]

   [:button.btn.btn-primary {:type "submit"
                             :on-click #(.log js/console (clj->js @state))}
    "Submit"]])



;; ------------------------------------------------------------------------------------
;; rendering-functions

;; one-style of rendering
;; if we want to render any component in body of index.html use this below function

;; (defn render-sample []
;;   (reagent/render-component [home]
;;                             (.-body js/document)))


;; another-style rendering a component using id
;; we use below method

;; (defn render-sample []
;;   (reagent/render-component [home]
;;                             (.getElementById js/document "app")))

;; (render-sample)

;; rendering using defcard
(defcard Reagent-form
  (reagent/as-element [home]))

;; ================================================================================
;; slider
;; ================================================================================

(defn slider [id label val min max]
  [::div
   [:label label]
   [:input#id {:type "number"
               :min min
               :max max
               :value @val
               :on-change #(reset! val (-> % .-target .-value))}]
   [:input#id {:type "range"
               :min min
               :max max
               :value @val
               :on-change #(reset! val (-> % .-target .-value))}]])


(defn mybody []
  (let [atom1 (reagent/atom nil)
        atom2 (reagent/atom nil)]
    (fn []
      [:div
       [slider "height" "height" atom1 1 250]
       [slider "weight" "weigth" atom2 1 500]])))

(defcard my-sliders
  (reagent/as-element [mybody]))


;; ====================================================================================
;; calculating BMI using sliders
;; ====================================================================================

(defn input-element1 [id type min max val]
  [:div
   [:input#id.form-control {:type type
                            :min min
                            :max max
                            :value (int (@val id))
                            :on-change (fn [e]
                                         (let [v (-> e .-target .-value)
                                               w (@val :weight)
                                               h (/ (@val :height) 100)
                                               b (@val :bmi)]
                                           (swap! val assoc id v)
                                           (case id
                                             :height (swap! val assoc :bmi (/ (* w 10000) (* v v)))
                                             :weight (swap! val assoc :bmi (/ v (* h h)))
                                             :bmi (swap! val assoc :weight (* v h h))))
                                         ;; (swap! val assoc id (-> e .-target .-value))
                                         )}]])

(defn myslider1 [id label min max val]
  [:div
   [:label label]
   [:div.row
    [:div.col-sm-2 [input-element1 id "number" min max val]]
    [:div.col-sm-10 [input-element1 id "range" min max val]]]])

(defn my-bmi-component []
  (let [my-data (reagent/atom {:height 180 :weight 80 :bmi 24 })]
    (fn []
      [:div
       [myslider1 :height "Height" 100 220 my-data]
       [myslider1 :weight "Weight" 30 150 my-data]
       [myslider1 :bmi "BMI" 10 60 my-data]
       ])))

(defcard bmi-component
  (reagent/as-element [my-bmi-component]))


;; =====================================================================================
;; form validation using bouncer.
;; =====================================================================================

(defn validator [data-set]
  (first (b/validate data-set
                     :username-email [[v/required :message "Field is required"]
                                      [v/email :message "Enter valid email-id"]]
                     :password [[v/required :message "Field is required"]
                                [v/string  :message "Enter valid password"]])))

(defn input-element2 [id ttype data-set placeholder in-focus]
  [:input#id.form-control {:type ttype
                           :value (@data-set id)
                           :placeholder placeholder
                           :on-change #(swap! data-set assoc id (-> % .-target .-value))
                           :on-blur  #(reset! in-focus true)
                           }])

(defn input-validate [id label span-class ttype data-set placeholder focus]
  (let [input-focus (reagent/atom false)]
    (fn []
      [:div.form-group
       [:div.col-md-12
        [:label label]
        [:div.input-group.col-sm-10
         [:span {:class span-class}]
         [input-element2 id ttype data-set placeholder input-focus]]
        (if (or @input-focus @focus)
          (if (= nil (validator @data-set))
            [:div]
            [:div {:style  {:color "red"}}
             [:b
              (str (first ((validator @data-set) id)))]]  )
          [:div])]])))

(defn button [value ttype data-set focus]
  [:div.form-group
   [:div.col-md-6
    [:button.btn.btn-primary {:type ttype
                              :on-click (fn []
                                          (if (= nil (validator @data-set))
                                            (js/alert "You are successfully Registered")
                                            (reset! focus true)))} value]]])

(defn log-in-form []
  (let [my-data (reagent/atom  {})
        focus (reagent/atom false)]
    (fn []
      [:div.container
       [:div.panel.panel-primary.modal-dialog
        [:div.panel-heading
         [:h2 "Log-in form"]]
        [:div.panel-body
         [input-validate :username-email "Username or Email"  "input-group-addon glyphicon glyphicon-user" "email" my-data "eg.,Siva or siva@***.com" focus]
         [input-validate :password "Password"  "input-group-addon glyphicon glyphicon-lock" "password" my-data "eg., .........." focus]
         [button "Sign-in" "button" my-data focus ]]]])))


(defcard log-in-form
  (reagent/as-element [log-in-form]))
