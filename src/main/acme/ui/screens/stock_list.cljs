(ns acme.ui.screens.stock-list
  "Stock list screen"
  (:require [reagent.core :as r]
            ["react-native" :refer [View Text FlatList TextInput Alert SafeAreaView]]
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
       :placeholder "Search stock items..."
       :clearButtonMode "while-editing"}]]))

(defn header []
  (let [item-count @(rf/subscribe [:stock/count])
        low-stock-items @(rf/subscribe [:stock/low-stock-items])
        low-stock-count (count low-stock-items)]
    [:> View {:style #js {:padding 16 :paddingTop 8}}
     [:> Text {:style (styles/get-style :title)} "Stock"]
     [:> View {:style (styles/get-style :row)}
      [:> Text {:style (styles/get-style :mutedText)}
       (str item-count " items")]
      (when (pos? low-stock-count)
        [:> Text {:style #js {:marginLeft 12
                              :color (:warning styles/colors)
                              :fontSize 14}}
         (str low-stock-count " low stock")])]]))

(defn stock-list-screen [{:keys [navigation]}]
  (let [items @(rf/subscribe [:stock/filtered-items])
        loading? @(rf/subscribe [:ui/loading?])]
    [:> SafeAreaView {:style (styles/get-style :safeArea)}
     [:> View {:style (styles/get-style :container)}
      [header]
      [search-bar]
      (if (and (empty? items) (not loading?))
        [item-card/empty-state {:message "No stock items yet.\nTap + to add your first item."}]
        [:> FlatList
         {:data (clj->js items)
          :key-extractor (fn [item] (.-id item))
          :render-item (fn [info]
                         (let [item (js->clj (.-item info) :keywordize-keys true)]
                           (r/as-element
                            [item-card/stock-item-card
                             {:item item
                              :on-press (fn [_]
                                          (.navigate navigation "ItemDetail"
                                                     #js {:itemId (:id item)
                                                          :itemType "stock"}))
                              :on-long-press (fn [item]
                                               (.alert Alert
                                                       "Add to Shopping List"
                                                       (str "Add \"" (:name item) "\" to your shopping list?")
                                                       #js [#js {:text "Cancel" :style "cancel"}
                                                            #js {:text "Add"
                                                                 :onPress #(rf/dispatch [:stock/move-to-needed (:id item)])}]))}])))
          :content-container-style #js {:padding 16 :paddingTop 0}
          :refreshing loading?
          :on-refresh #(rf/dispatch [:app/start-listeners])}])
      [button/fab {:on-press #(.navigate navigation "ItemDetail"
                                         #js {:itemType "stock"})}]]]))
