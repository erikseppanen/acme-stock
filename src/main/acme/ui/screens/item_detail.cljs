(ns acme.ui.screens.item-detail
  "Item detail/edit screen"
  (:require [reagent.core :as r]
            ["react-native" :refer [View Text SafeAreaView Alert]]
            [re-frame.core :as rf]
            [acme.ui.styles :as styles]
            [acme.ui.components.item-form :as item-form]
            [acme.ui.components.button :as button]))

(defn item-detail-screen [{:keys [navigation route]}]
  (let [params (.-params route)
        item-id (when params (.-itemId params))
        item-type (when params (.-itemType params))
        is-stock? (= item-type "stock")
        is-new? (nil? item-id)
        existing-item (when item-id
                        (if is-stock?
                          @(rf/subscribe [:stock/item-by-id item-id])
                          @(rf/subscribe [:needed/item-by-id item-id])))]
    [:> SafeAreaView {:style (styles/get-style :safeArea)}
     [:> View {:style (styles/get-style :container)}
      ;; Header
      [:> View {:style #js {:padding 16
                            :flexDirection "row"
                            :justifyContent "space-between"
                            :alignItems "center"}}
       [:> Text {:style (styles/get-style :title)}
        (cond
          (and is-new? is-stock?) "Add Stock Item"
          (and is-new? (not is-stock?)) "Add to Shopping List"
          is-stock? "Edit Stock Item"
          :else "Edit Shopping Item")]
       (when (not is-new?)
         [button/danger-button
          {:title "Delete"
           :on-press (fn []
                       (.alert Alert
                               "Delete Item"
                               "Are you sure you want to delete this item?"
                               #js [#js {:text "Cancel" :style "cancel"}
                                    #js {:text "Delete"
                                         :style "destructive"
                                         :onPress (fn []
                                                    (if is-stock?
                                                      (rf/dispatch [:stock/delete-item item-id])
                                                      (rf/dispatch [:needed/delete-item item-id]))
                                                    (.goBack navigation))}]))
           :style #js {:paddingVertical 8 :paddingHorizontal 12}}])]
      ;; Form
      (if is-stock?
        [item-form/stock-item-form
         {:initial-values existing-item
          :on-submit (fn [values]
                       (if is-new?
                         (rf/dispatch [:stock/add-item values])
                         (rf/dispatch [:stock/update-item (assoc values :id item-id)]))
                       (.goBack navigation))
          :on-cancel #(.goBack navigation)}]
        [item-form/needed-item-form
         {:initial-values existing-item
          :on-submit (fn [values]
                       (if is-new?
                         (rf/dispatch [:needed/add-item values])
                         (rf/dispatch [:needed/update-item (assoc values :id item-id)]))
                       (.goBack navigation))
          :on-cancel #(.goBack navigation)}])]]))
