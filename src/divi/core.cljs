(ns divi.core
    (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(println "This text is printed from src/divi/core.cljs. Go ahead and edit it and see reloading in action.")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:text "Hej Nora"
                          :visa-svar false
                          :data {}}))

(defn mata-in [[c r]]
  (fn [e]
    (.log js/console e)
    (.preventDefault e) 
    (let [nytt-värde (-> e
                         .-target
                         .-value)]
      (swap! app-state 
             assoc-in
             [:data c r]
             nytt-värde))
    nil))

(defn multiplikationstabell []
  [:table {:style {:font-family "monospace"}}
   [:tbody
    (concat (doall (map (fn [r]
                          [:tr {:key (str "*" r)}
                           (cons
                            [:td {:style {:color :red
                                          :text-align "center"
                                          :font-weight :bold}
                                  :key (str r)}
                             r]
                            (doall (map (fn [c]
                                          [:td {:key (str c "*" r)
                                                :style {:width "2rem"
                                                        :text-align "right"
                                                        :border "solid"
                                                        :border-width "1px"}}
                                           (if (:visa-svar @app-state)
                                             [:div {:style {:height "18.25px"
                                                            :width "100%"}}
                                              (* c r)]
                                             [:input {:type :text
                                                      :value (get-in @app-state
                                                                     [:data c r] "")
                                                      :on-input (mata-in [c r])
                                                      :style {:padding "0px"
                                                              :height "17px"
                                                              :width "100%"}}])])
                                        (range 1 11))))])
                        (range 10 0 -1)))
            [[:tr {:key "*0"}
              (cons
               [:td {:style {:color :red
                             :font-weight :bold}
                     :key "origo"}
                0]
               (map (fn [c]
                      [:td {:style {:color :red
                                    :text-align "center"
                                    :font-weight :bold}
                            :key (str c "*")}
                       c])
                    (range 1 11)))] ])]])

(defn hello-world []
  [:div
   [:h1 (:text @app-state)]
   [:h3 "Detta är multiplikationstabellen"]
   [:div
    [:input {:type "checkbox" :id "visa"
             :checked (:visa-svar @app-state)
             :on-change #(swap! app-state update :visa-svar not)}]
    [:label {:for "visa"}] "Visa svaren"]
   [multiplikationstabell]])

(reagent/render-component [hello-world]
                          (. js/document (getElementById "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
