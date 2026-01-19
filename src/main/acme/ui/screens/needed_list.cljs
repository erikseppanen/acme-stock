(ns acme.ui.screens.needed-list
  "Needed/shopping list screen"
  (:require [reagent.core :as r]
            ["react-native" :refer [View Text FlatList TextInput SafeAreaView]]
            [re-frame.core :as rf]
            [acme.ui.styles :as styles]
            [acme.ui.components.item-card :as item-card]
            [acme.ui.components.button :as button]))

(defn search-bar []
  (let [query @(rf/subscribe [:ui/search-query])]
    [:> View {:style #js {:padding 16 :paddingBottom 8}}
     [:> TextInput
      {:style #js [(styles/get-style :textInput)
                   #js {:backgroundColor (:white styles/colors)}]
       :value query
       :on-change-text #(rf/dispatch [:ui/set-search-query %])
       :placeholder "Search shopping list..."
       :clearButtonMode "while-editing"}]]))

(defn header []
  (let [total-count @(rf/subscribe [:needed/count])
        unchecked-count @(rf/subscribe [:needed/unchecked-count])
        has-checked? @(rf/subscribe [:needed/has-checked?])]
    [:> View {:style #js {:padding 16 :paddingTop 8}}
     [:> View {:style (styles/get-style :spaceBetween)}
      [:> View
       [:> Text {:style (styles/get-style :title)} "Shopping List"]
       [:> Text {:style (styles/get-style :mutedText)}
        (str unchecked-count " of " total-count " remaining")]]
      (when has-checked?
        [button/secondary-button
         {:title "Clear Done"
          :on-press #(rf/dispatch [:needed/clear-checked])
          :style #js {:paddingVertical 8 :paddingHorizontal 12}}])]]))

(defn needed-list-screen [{:keys [navigation]}]
  (let [items @(rf/subscribe [:needed/filtered-items])
        loading? @(rf/subscribe [:ui/loading?])]
    [:> SafeAreaView {:style (styles/get-style :safeArea)}
     [:> View {:style (styles/get-style :container)}
      [header]
      [search-bar]
      (if (and (empty? items) (not loading?))
        [item-card/empty-state {:message "Shopping list is empty.\nLong-press stock items to add them here,\nor tap + to add directly."}]
        [:> FlatList
         {:data (clj->js items)
          :key-extractor (fn [item] (.-id item))
          :render-item (fn [info]
                         (let [item (js->clj (.-item info) :keywordize-keys true)]
                           (r/as-element
                            [item-card/needed-item-card
                             {:item item
                              :on-press (fn [_]
                                          (.navigate navigation "ItemDetail"
                                                     #js {:itemId (:id item)
                                                          :itemType "needed"}))
                              :on-toggle (fn [id]
                                           (rf/dispatch [:needed/toggle-checked id]))}])))
          :content-container-style #js {:padding 16 :paddingTop 0}
          :refreshing loading?
          :on-refresh #(rf/dispatch [:app/start-listeners])}])
      [button/fab {:on-press #(.navigate navigation "ItemDetail"
                                         #js {:itemType "needed"})}]]]))
