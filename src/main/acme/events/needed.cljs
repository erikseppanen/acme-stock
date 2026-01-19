(ns acme.events.needed
  "Shopping list (needed items) CRUD events"
  (:require [re-frame.core :as rf]))

(defn generate-id []
  (str (random-uuid)))

(defn timestamp []
  (.now js/Date))

;; Add new needed item
(rf/reg-event-fx
 :needed/add-item
 (fn [{:keys [db]} [_ {:keys [name quantity unit]}]]
   (let [id (generate-id)
         now (timestamp)
         item {:id id
               :name name
               :quantity quantity
               :unit unit
               :checked? false
               :stock-item-id nil
               :created-at now
               :updated-at now}]
     {:db (-> db
              (assoc-in [:needed :items id] item)
              (update-in [:needed :order] #(vec (cons id %))))
      :firebase/add-needed-item {:item item
                                 :on-error [:app/error]}})))

;; Update existing needed item
(rf/reg-event-fx
 :needed/update-item
 (fn [{:keys [db]} [_ {:keys [id] :as updates}]]
   (let [existing (get-in db [:needed :items id])
         updated (merge existing updates {:updated-at (timestamp)})]
     {:db (assoc-in db [:needed :items id] updated)
      :firebase/update-needed-item {:item updated
                                    :on-error [:app/error]}})))

;; Delete needed item
(rf/reg-event-fx
 :needed/delete-item
 (fn [{:keys [db]} [_ id]]
   {:db (-> db
            (update-in [:needed :items] dissoc id)
            (update-in [:needed :order] #(vec (remove #{id} %))))
    :firebase/delete-needed-item {:id id
                                  :on-error [:app/error]}}))

;; Toggle item checked state
(rf/reg-event-fx
 :needed/toggle-checked
 (fn [{:keys [db]} [_ id]]
   (let [existing (get-in db [:needed :items id])
         updated (-> existing
                     (update :checked? not)
                     (assoc :updated-at (timestamp)))]
     {:db (assoc-in db [:needed :items id] updated)
      :firebase/update-needed-item {:item updated
                                    :on-error [:app/error]}})))

;; Clear all checked items
(rf/reg-event-fx
 :needed/clear-checked
 (fn [{:keys [db]} _]
   (let [checked-ids (->> (get-in db [:needed :items])
                          (filter (fn [[_ item]] (:checked? item)))
                          (map first)
                          vec)]
     (if (seq checked-ids)
       {:db (-> db
                (update-in [:needed :items] #(apply dissoc % checked-ids))
                (update-in [:needed :order] #(vec (remove (set checked-ids) %))))
        :firebase/delete-checked-needed-items {:ids checked-ids
                                               :on-error [:app/error]}}
       {:db db}))))

;; Local-only: set needed items from sync
(rf/reg-event-db
 :needed/set-items
 (fn [db [_ items]]
   (let [items-map (into {} (map (juxt :id identity) items))
         order (mapv :id items)]
     (-> db
         (assoc-in [:needed :items] items-map)
         (assoc-in [:needed :order] order)))))
