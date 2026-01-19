(ns acme.events.sync
  "Firestore sync events"
  (:require [re-frame.core :as rf]))

;; Handle stock items received from Firestore listener
(rf/reg-event-fx
 :sync/stock-items-received
 (fn [{:keys [db]} [_ items]]
   {:db (-> db
            (assoc-in [:ui :loading?] false)
            (assoc-in [:sync :last-sync] (.now js/Date))
            (assoc-in [:sync :connected?] true))
    :dispatch [:stock/set-items items]}))

;; Handle needed items received from Firestore listener
(rf/reg-event-fx
 :sync/needed-items-received
 (fn [{:keys [db]} [_ items]]
   {:db (-> db
            (assoc-in [:ui :loading?] false)
            (assoc-in [:sync :last-sync] (.now js/Date))
            (assoc-in [:sync :connected?] true))
    :dispatch [:needed/set-items items]}))

;; Mark connection as disconnected
(rf/reg-event-db
 :sync/disconnected
 (fn [db _]
   (assoc-in db [:sync :connected?] false)))

;; Mark connection as connected
(rf/reg-event-db
 :sync/connected
 (fn [db _]
   (assoc-in db [:sync :connected?] true)))

;; Add pending write for offline support
(rf/reg-event-db
 :sync/add-pending-write
 (fn [db [_ write-op]]
   (update-in db [:sync :pending-writes] conj write-op)))

;; Clear pending write after successful sync
(rf/reg-event-db
 :sync/clear-pending-write
 (fn [db [_ write-id]]
   (update-in db [:sync :pending-writes]
              (fn [writes]
                (vec (remove #(= (:id %) write-id) writes))))))
