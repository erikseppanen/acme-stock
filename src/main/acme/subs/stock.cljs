(ns acme.subs.stock
  "Stock item subscriptions"
  (:require [re-frame.core :as rf]
            [clojure.string :as str]))

;; Get all stock items as a map
(rf/reg-sub
 :stock/items-map
 (fn [db _]
   (get-in db [:stock :items])))

;; Get stock items order
(rf/reg-sub
 :stock/order
 (fn [db _]
   (get-in db [:stock :order])))

;; Get search query
(rf/reg-sub
 :ui/search-query
 (fn [db _]
   (get-in db [:ui :search-query])))

;; Get all stock items as ordered list
(rf/reg-sub
 :stock/items-list
 :<- [:stock/items-map]
 :<- [:stock/order]
 (fn [[items-map order] _]
   (mapv #(get items-map %) order)))

;; Get filtered stock items based on search query
(rf/reg-sub
 :stock/filtered-items
 :<- [:stock/items-list]
 :<- [:ui/search-query]
 (fn [[items query] _]
   (if (str/blank? query)
     items
     (filter #(str/includes?
               (str/lower-case (:name %))
               (str/lower-case query))
             items))))

;; Get single stock item by id
(rf/reg-sub
 :stock/item-by-id
 :<- [:stock/items-map]
 (fn [items-map [_ id]]
   (get items-map id)))

;; Get items that are below minimum quantity
(rf/reg-sub
 :stock/low-stock-items
 :<- [:stock/items-list]
 (fn [items _]
   (filter (fn [item]
             (and (:min-quantity item)
                  (:quantity item)
                  (< (:quantity item) (:min-quantity item))))
           items)))

;; Get count of stock items
(rf/reg-sub
 :stock/count
 :<- [:stock/items-map]
 (fn [items-map _]
   (count items-map)))

;; Get stock items grouped by category
(rf/reg-sub
 :stock/items-by-category
 :<- [:stock/items-list]
 (fn [items _]
   (group-by #(or (:category %) "Uncategorized") items)))
