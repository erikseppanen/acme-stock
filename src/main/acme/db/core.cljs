(ns acme.db.core
  "Initial app-db state and helpers")

(def default-db
  {:stock    {:items {}     ;; Map of id -> stock item
              :order []}    ;; Display ordering (list of ids)
   :needed   {:items {}     ;; Map of id -> shopping item
              :order []}    ;; Display ordering (list of ids)
   :ui       {:current-screen :stock-list
              :editing-item   nil
              :search-query   ""
              :loading?       false}
   :sync     {:pending-writes []
              :last-sync      nil
              :connected?     false}})
