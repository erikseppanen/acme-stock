(ns acme.ui.components.item-form
  "Form component for adding/editing items"
  (:require [reagent.core :as r]
            ["react-native" :refer [View Text TextInput ScrollView KeyboardAvoidingView Platform]]
            [re-frame.core :as rf]
            [acme.ui.styles :as styles]
            [acme.ui.components.button :as button]))

(defn text-field
  "Text input field with label"
  [{:keys [label value on-change placeholder keyboard-type]}]
  [:> View {:style #js {:marginBottom 16}}
   [:> Text {:style #js {:fontSize 14
                         :fontWeight "600"
                         :color (:text-secondary styles/colors)
                         :marginBottom 6}}
    label]
   [:> TextInput
    {:style (styles/get-style :textInput)
     :value (or value "")
     :on-change-text on-change
     :placeholder placeholder
     :keyboard-type (or keyboard-type "default")}]])

(defn number-field
  "Number input field with label"
  [{:keys [label value on-change placeholder]}]
  [:> View {:style #js {:marginBottom 16}}
   [:> Text {:style #js {:fontSize 14
                         :fontWeight "600"
                         :color (:text-secondary styles/colors)
                         :marginBottom 6}}
    label]
   [:> TextInput
    {:style (styles/get-style :textInput)
     :value (if value (str value) "")
     :on-change-text #(on-change (when (seq %) (js/parseInt % 10)))
     :placeholder placeholder
     :keyboard-type "numeric"}]])

(defn stock-item-form
  "Form for creating/editing stock items"
  [{:keys [initial-values on-submit on-cancel]}]
  (let [form-state (r/atom (or initial-values
                               {:name ""
                                :quantity nil
                                :min-quantity nil
                                :unit ""
                                :category ""
                                :notes ""}))]
    (fn [{:keys [on-submit on-cancel]}]
      [:> KeyboardAvoidingView
       {:style #js {:flex 1}
        :behavior (if (= (.-OS Platform) "ios") "padding" "height")}
       [:> ScrollView {:style #js {:flex 1 :padding 16}}
        [text-field {:label "Name *"
                     :value (:name @form-state)
                     :on-change #(swap! form-state assoc :name %)
                     :placeholder "Enter item name"}]
        [:> View {:style #js {:flexDirection "row" :gap 12}}
         [:> View {:style #js {:flex 1}}
          [number-field {:label "Quantity"
                         :value (:quantity @form-state)
                         :on-change #(swap! form-state assoc :quantity %)
                         :placeholder "0"}]]
         [:> View {:style #js {:flex 1}}
          [number-field {:label "Min Quantity"
                         :value (:min-quantity @form-state)
                         :on-change #(swap! form-state assoc :min-quantity %)
                         :placeholder "0"}]]]
        [text-field {:label "Unit"
                     :value (:unit @form-state)
                     :on-change #(swap! form-state assoc :unit %)
                     :placeholder "e.g., pcs, kg, L"}]
        [text-field {:label "Category"
                     :value (:category @form-state)
                     :on-change #(swap! form-state assoc :category %)
                     :placeholder "e.g., Groceries, Household"}]
        [text-field {:label "Notes"
                     :value (:notes @form-state)
                     :on-change #(swap! form-state assoc :notes %)
                     :placeholder "Additional notes..."}]
        [:> View {:style #js {:flexDirection "row"
                              :justifyContent "space-between"
                              :marginTop 24
                              :gap 12}}
         [:> View {:style #js {:flex 1}}
          [button/secondary-button {:title "Cancel"
                                    :on-press on-cancel}]]
         [:> View {:style #js {:flex 1}}
          [button/primary-button {:title "Save"
                                  :on-press #(on-submit @form-state)
                                  :disabled? (empty? (:name @form-state))}]]]]])))

(defn needed-item-form
  "Form for creating/editing needed items"
  [{:keys [initial-values on-submit on-cancel]}]
  (let [form-state (r/atom (or initial-values
                               {:name ""
                                :quantity nil
                                :unit ""}))]
    (fn [{:keys [on-submit on-cancel]}]
      [:> KeyboardAvoidingView
       {:style #js {:flex 1}
        :behavior (if (= (.-OS Platform) "ios") "padding" "height")}
       [:> ScrollView {:style #js {:flex 1 :padding 16}}
        [text-field {:label "Name *"
                     :value (:name @form-state)
                     :on-change #(swap! form-state assoc :name %)
                     :placeholder "Enter item name"}]
        [:> View {:style #js {:flexDirection "row" :gap 12}}
         [:> View {:style #js {:flex 1}}
          [number-field {:label "Quantity"
                         :value (:quantity @form-state)
                         :on-change #(swap! form-state assoc :quantity %)
                         :placeholder "0"}]]
         [:> View {:style #js {:flex 1}}
          [text-field {:label "Unit"
                       :value (:unit @form-state)
                       :on-change #(swap! form-state assoc :unit %)
                       :placeholder "e.g., pcs"}]]]
        [:> View {:style #js {:flexDirection "row"
                              :justifyContent "space-between"
                              :marginTop 24
                              :gap 12}}
         [:> View {:style #js {:flex 1}}
          [button/secondary-button {:title "Cancel"
                                    :on-press on-cancel}]]
         [:> View {:style #js {:flex 1}}
          [button/primary-button {:title "Add"
                                  :on-press #(on-submit @form-state)
                                  :disabled? (empty? (:name @form-state))}]]]]])))
