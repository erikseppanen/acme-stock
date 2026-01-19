(ns acme.ui.components.button
  "Reusable button component"
  (:require [reagent.core :as r]
            ["react-native" :refer [TouchableOpacity Text View ActivityIndicator]]
            [acme.ui.styles :as styles]))

(defn primary-button
  "Primary action button"
  [{:keys [on-press title disabled? loading? style]}]
  [:> TouchableOpacity
   {:style (styles/get-style :button)
    :onPress (when-not disabled? on-press)
    :disabled (boolean (or disabled? loading?))
    :accessible true
    :accessibilityRole "button"
    :accessibilityLabel title}
   (if loading?
     [:> ActivityIndicator {:color (:white styles/colors)}]
     [:> Text {:style (styles/get-style :buttonText)} title])])

(defn secondary-button
  "Secondary action button"
  [{:keys [on-press title disabled? loading? style]}]
  [:> TouchableOpacity
   {:style (styles/get-style :buttonSecondary)
    :onPress (when-not disabled? on-press)
    :disabled (boolean (or disabled? loading?))
    :accessible true
    :accessibilityRole "button"
    :accessibilityLabel title}
   (if loading?
     [:> ActivityIndicator {:color (:text-primary styles/colors)}]
     [:> Text {:style (styles/get-style :buttonSecondaryText)} title])])

(defn danger-button
  "Danger/destructive action button"
  [{:keys [on-press title disabled? loading? style]}]
  [:> TouchableOpacity
   {:style (styles/get-style :buttonDanger)
    :onPress (fn [] (when on-press (on-press)))
    :disabled (or disabled? loading?)}
   (if loading?
     [:> ActivityIndicator {:color (:white styles/colors)}]
     [:> Text {:style (styles/get-style :buttonText)} title])])

(defn icon-button
  "Icon button for actions"
  [{:keys [on-press icon-text style]}]
  [:> TouchableOpacity
   {:style #js {:padding 8}
    :onPress (fn [] (when on-press (on-press)))}
   [:> Text {:style #js {:fontSize 20}} icon-text]])

(defn fab
  "Floating action button"
  [{:keys [on-press]}]
  [:> TouchableOpacity
   {:style (styles/get-style :fab)
    :onPress (fn [] (when on-press (on-press)))}
   [:> Text {:style (styles/get-style :fabText)} "+"]])
