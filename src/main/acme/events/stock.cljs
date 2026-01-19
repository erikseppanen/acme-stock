(ns acme.events.stock
  "Stock item CRUD events"
  (:require [re-frame.core :as rf]))

(defn generate-id []
  (str (random-uuid)))

(defn timestamp []
  (.now js/Date))

;; Add new stock item
(rf/reg-event-fx
 :stock/add-item
 (fn [{:keys [db]} [_ {:keys [name quantity min-quantity unit category notes]}]]
   (let [id (generate-id)
         now (timestamp)
         item {:id id
               :name name
               :quantity quantity
               :min-quantity min-quantity
               :unit unit
               :category category
               :notes notes
               :created-at now
               :updated-at now}]
     {:db (-> db
              (assoc-in [:stock :items id] item)
              (update-in [:stock :order] #(vec (cons id %))))
      :firebase/add-stock-item {:item item
                                :on-error [:app/error]}})))

;; Update existing stock item
(rf/reg-event-fx
 :stock/update-item
 (fn [{:keys [db]} [_ {:keys [id] :as updates}]]
   (let [existing (get-in db [:stock :items id])
         updated (merge existing updates {:updated-at (timestamp)})]
     {:db (assoc-in db [:stock :items id] updated)
      :firebase/update-stock-item {:item updated
                                   :on-error [:app/error]}})))

;; Delete stock item
(rf/reg-event-fx
 :stock/delete-item
 (fn [{:keys [db]} [_ id]]
   {:db (-> db
            (update-in [:stock :items] dissoc id)
            (update-in [:stock :order] #(vec (remove #{id} %))))
    :firebase/delete-stock-item {:id id
                                 :on-error [:app/error]}}))

;; Move stock item to needed list
(rf/reg-event-fx
 :stock/move-to-needed
 (fn [{:keys [db]} [_ stock-id]]
   (let [stock-item (get-in db [:stock :items stock-id])
         needed-id (generate-id)
         now (timestamp)
         needed-item {:id needed-id
                      :name (:name stock-item)
                      :quantity (:quantity stock-item)
                      :unit (:unit stock-item)
                      :checked? false
                      :stock-item-id stock-id
                      :created-at now
                      :updated-at now}]
     {:db (-> db
              (assoc-in [:needed :items needed-id] needed-item)
              (update-in [:needed :order] #(vec (cons needed-id %))))
      :firebase/add-needed-item {:item needed-item
                                 :on-error [:app/error]}})))

;; Update stock quantity
(rf/reg-event-fx
 :stock/update-quantity
 (fn [{:keys [db]} [_ id quantity]]
   (let [existing (get-in db [:stock :items id])
         updated (assoc existing
                        :quantity quantity
                        :updated-at (timestamp))]
     {:db (assoc-in db [:stock :items id] updated)
      :firebase/update-stock-item {:item updated
                                   :on-error [:app/error]}})))

;; Local-only: set stock items from sync
(rf/reg-event-db
 :stock/set-items
 (fn [db [_ items]]
   (let [items-map (into {} (map (juxt :id identity) items))
         order (mapv :id items)]
     (-> db
         (assoc-in [:stock :items] items-map)
         (assoc-in [:stock :order] order)))))
