(ns acme.db.schema
  "Malli schemas for data validation"
  (:require [malli.core :as m]))

;; Stock item schema
(def StockItem
  [:map
   [:id :string]
   [:name :string]
   [:quantity {:optional true} [:maybe :int]]
   [:min-quantity {:optional true} [:maybe :int]]
   [:unit {:optional true} [:maybe :string]]
   [:category {:optional true} [:maybe :string]]
   [:notes {:optional true} [:maybe :string]]
   [:created-at :int]
   [:updated-at :int]])

;; Shopping/needed item schema
(def NeededItem
  [:map
   [:id :string]
   [:name :string]
   [:quantity {:optional true} [:maybe :int]]
   [:unit {:optional true} [:maybe :string]]
   [:checked? :boolean]
   [:stock-item-id {:optional true} [:maybe :string]]
   [:created-at :int]
   [:updated-at :int]])

;; UI state schema
(def UIState
  [:map
   [:current-screen [:enum :stock-list :needed-list :item-detail]]
   [:editing-item [:maybe :string]]
   [:search-query :string]
   [:loading? :boolean]])

;; Sync state schema
(def SyncState
  [:map
   [:pending-writes [:vector :map]]
   [:last-sync [:maybe :int]]
   [:connected? :boolean]])

;; Full app-db schema
(def AppDB
  [:map
   [:stock [:map
            [:items [:map-of :string StockItem]]
            [:order [:vector :string]]]]
   [:needed [:map
             [:items [:map-of :string NeededItem]]
             [:order [:vector :string]]]]
   [:ui UIState]
   [:sync SyncState]])

;; Validation helpers
(defn valid-stock-item? [item]
  (m/validate StockItem item))

(defn valid-needed-item? [item]
  (m/validate NeededItem item))

(defn valid-db? [db]
  (m/validate AppDB db))
