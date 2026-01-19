(ns acme.ui.components.item-card
  "Item card component for displaying stock and needed items"
  (:require [reagent.core :as r]
            ["react-native" :refer [View Text TouchableOpacity Pressable]]
            [re-frame.core :as rf]
            [acme.ui.styles :as styles]))

(defn stock-item-card
  "Card for displaying a stock item"
  [{:keys [item on-press on-long-press]}]
  (let [{:keys [id name quantity min-quantity unit category]} item
        low-stock? (and min-quantity quantity (< quantity min-quantity))]
    [:> Pressable
     {:style #js [(styles/get-style :card)
                  (when low-stock?
                    #js {:borderLeftWidth 4
                         :borderLeftColor (:warning styles/colors)})]
      :on-press #(when on-press (on-press item))
      :on-long-press #(when on-long-press (on-long-press item))}
     [:> View {:style (styles/get-style :spaceBetween)}
      [:> View {:style #js {:flex 1}}
       [:> Text {:style (styles/get-style :subtitle)} name]
       (when category
         [:> Text {:style (styles/get-style :mutedText)} category])]
      [:> View {:style #js {:alignItems "flex-end"}}
       [:> Text {:style #js {:fontSize 18
                             :fontWeight "600"
                             :color (if low-stock?
                                      (:warning styles/colors)
                                      (:text-primary styles/colors))}}
        (str (or quantity 0) (when unit (str " " unit)))]
       (when min-quantity
         [:> Text {:style (styles/get-style :mutedText)}
          (str "Min: " min-quantity)])]]]))

(defn needed-item-card
  "Card for displaying a needed/shopping list item"
  [{:keys [item on-press on-toggle]}]
  (let [{:keys [id name quantity unit checked?]} item]
    [:> Pressable
     {:style #js [(styles/get-style :card)
                  (when checked?
                    #js {:opacity 0.6})]
      :on-press #(when on-press (on-press item))}
     [:> View {:style (styles/get-style :row)}
      ;; Checkbox
      [:> TouchableOpacity
       {:style #js {:width 28
                    :height 28
                    :borderRadius 14
                    :borderWidth 2
                    :borderColor (if checked?
                                   (:success styles/colors)
                                   (:gray-400 styles/colors))
                    :backgroundColor (if checked?
                                       (:success styles/colors)
                                       "transparent")
                    :alignItems "center"
                    :justifyContent "center"
                    :marginRight 12}
        :on-press #(when on-toggle (on-toggle id))}
       (when checked?
         [:> Text {:style #js {:color (:white styles/colors)
                               :fontSize 16
                               :fontWeight "bold"}}
          "✓"])]
      ;; Item info
      [:> View {:style #js {:flex 1}}
       [:> Text {:style #js [(styles/get-style :subtitle)
                             (when checked?
                               #js {:textDecorationLine "line-through"
                                    :color (:text-muted styles/colors)})]}
        name]
       (when (and quantity (pos? quantity))
         [:> Text {:style (styles/get-style :mutedText)}
          (str quantity (when unit (str " " unit)))])]]]))

(defn empty-state
  "Empty state component when no items exist"
  [{:keys [message]}]
  [:> View {:style (styles/get-style :emptyState)}
   [:> Text {:style (styles/get-style :emptyStateText)} message]])
