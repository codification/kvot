(ns divi.core
  (:require [reagent.core :as reagent :refer [atom]]
            [clojure.string :refer [trim]]))

(enable-console-print!)

(println "This text is printed from src/divi/core.cljs. Go ahead and edit it and see reloading in action.")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:text "Hej Nora"
                          :visa-svar false
                          :data {}}))

(def first-number (partial re-find #"[0-9]{1,2}"))

(defn mata-in [app-state [c r]]
  (fn [e]
    (.preventDefault e)
    (.stopPropagation e)
    (when-not (:visa-svar @app-state)
      (let [nytt-värde (-> e
                           .-target
                           .-value
                           trim
                           first-number)]
        (.log js/console "fick: " (-> e
                                      .-target
                                      .-value))
        (.log js/console "Nytt värde" (pr-str [c r]) ":" nytt-värde)
        (swap! app-state 
               assoc-in
               [:data c r]
               nytt-värde)))
    nil))

(defn cell-value-for [state [c r]]
  (if (:visa-svar state)
    (* c r)
    (get-in state
            [:data c r] "")))

(defn multiplikationstabell [app-state]
  (let [state @app-state]
    [:div
     [:h4 "Multiplikation"]
     [:table
      [:tbody
       (concat (map (fn [r]
                      [:tr {:key (str "*" r)}
                       (cons
                        [:td.column {:key (str r)}
                         r]
                        (map (fn [c]
                               [:td.cell {:key (str c "*" r)}
                                [:input {:type :text
                                         :size "2"
                                         :on-change (mata-in app-state [c r])
                                         :value (cell-value-for state [c r])}]])
                             (range 1 11)))])
                    (range 10 0 -1))
               [[:tr {:key "*0"}
                 (cons
                  [:td.origo {:key "origo"} 0]
                  (map (fn [c]
                         [:td.bottom-row {:key (str c "*")} c])
                       (range 1 11)))] ])]]]))

(defn division [app-state]
  (let [summa 123.3
        divisor 3
        resultat (/ summa divisor)]
    [:div
     [:h4 "Division"]
     [:div {:style {:font-weight "bold"
                    :width "33%"
                    :text-align "center"
                    :display "flex"
                    :align-items "center"
                    :justify-content "space-around"}}
      [:div {:style {:width "3rem"}}
       [:div {:style {:border-bottom "solid"}} summa] 
       [:div divisor]]
      [:div {:style {:font-weight "bold"}} "="]
      [:div {:style {}} [:input {:type :text
                                 :size 2
                                 :value (if (:visa-svar @app-state)
                                          resultat
                                          "")}]]]]))

(defn hello-world []
  (let [state @app-state]
    [:div
     [:h1 (:text state)]
     [:h3 "Här kan du öva"]
     [:div 
      [:input {:type "checkbox" :id "visa"
               :checked (:visa-svar state)
               :on-change #(swap! app-state update :visa-svar not)}]
      [:label {:for "visa"}] "Visa svaren"]
     [multiplikationstabell app-state]
     [:hr]
     [division app-state]]))

(reagent/render-component [hello-world]
                          (. js/document (getElementById "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
