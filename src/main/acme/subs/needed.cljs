(ns acme.subs.needed
  "Needed (shopping list) item subscriptions"
  (:require [re-frame.core :as rf]
            [clojure.string :as str]))

;; Get all needed items as a map
(rf/reg-sub
 :needed/items-map
 (fn [db _]
   (get-in db [:needed :items])))

;; Get needed items order
(rf/reg-sub
 :needed/order
 (fn [db _]
   (get-in db [:needed :order])))

;; Get all needed items as ordered list
(rf/reg-sub
 :needed/items-list
 :<- [:needed/items-map]
 :<- [:needed/order]
 (fn [[items-map order] _]
   (mapv #(get items-map %) order)))

;; Get filtered needed items based on search query
(rf/reg-sub
 :needed/filtered-items
 :<- [:needed/items-list]
 :<- [:ui/search-query]
 (fn [[items query] _]
   (if (str/blank? query)
     items
     (filter #(str/includes?
               (str/lower-case (:name %))
               (str/lower-case query))
             items))))

;; Get unchecked items
(rf/reg-sub
 :needed/unchecked-items
 :<- [:needed/items-list]
 (fn [items _]
   (filter #(not (:checked? %)) items)))

;; Get checked items
(rf/reg-sub
 :needed/checked-items
 :<- [:needed/items-list]
 (fn [items _]
   (filter :checked? items)))

;; Get single needed item by id
(rf/reg-sub
 :needed/item-by-id
 :<- [:needed/items-map]
 (fn [items-map [_ id]]
   (get items-map id)))

;; Get count of needed items
(rf/reg-sub
 :needed/count
 :<- [:needed/items-map]
 (fn [items-map _]
   (count items-map)))

;; Get count of unchecked items
(rf/reg-sub
 :needed/unchecked-count
 :<- [:needed/unchecked-items]
 (fn [items _]
   (count items)))

;; Get count of checked items
(rf/reg-sub
 :needed/checked-count
 :<- [:needed/checked-items]
 (fn [items _]
   (count items)))

;; Check if there are any checked items (for showing clear button)
(rf/reg-sub
 :needed/has-checked?
 :<- [:needed/checked-count]
 (fn [count _]
   (pos? count)))

;; Get UI state
(rf/reg-sub
 :ui/loading?
 (fn [db _]
   (get-in db [:ui :loading?])))

(rf/reg-sub
 :ui/editing-item
 (fn [db _]
   (get-in db [:ui :editing-item])))

(rf/reg-sub
 :ui/current-screen
 (fn [db _]
   (get-in db [:ui :current-screen])))

;; Get sync state
(rf/reg-sub
 :sync/connected?
 (fn [db _]
   (get-in db [:sync :connected?])))

(rf/reg-sub
 :sync/last-sync
 (fn [db _]
   (get-in db [:sync :last-sync])))
