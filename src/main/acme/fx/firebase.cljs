(ns acme.fx.firebase
  "Re-frame effect handlers for Firebase operations"
  (:require [re-frame.core :as rf]
            [acme.firebase.firestore :as firestore]
            [acme.firebase.listeners :as listeners]
            [acme.firebase.config :as config]))

;; Initialize Firebase
(rf/reg-fx
 :firebase/init
 (fn [_]
   (config/init-firebase!)))

;; Subscribe to real-time updates
(rf/reg-fx
 :firebase/subscribe
 (fn [_]
   (listeners/subscribe-all!)))

;; Unsubscribe from real-time updates
(rf/reg-fx
 :firebase/unsubscribe
 (fn [_]
   (listeners/unsubscribe-all!)))

;; Stock item effects

(rf/reg-fx
 :firebase/add-stock-item
 (fn [{:keys [item on-success on-error]}]
   (firestore/add-stock-item!
    item
    (fn [item] (when on-success (rf/dispatch (conj on-success item))))
    (fn [err] (when on-error (rf/dispatch (conj on-error err)))))))

(rf/reg-fx
 :firebase/update-stock-item
 (fn [{:keys [item on-success on-error]}]
   (firestore/update-stock-item!
    item
    (fn [item] (when on-success (rf/dispatch (conj on-success item))))
    (fn [err] (when on-error (rf/dispatch (conj on-error err)))))))

(rf/reg-fx
 :firebase/delete-stock-item
 (fn [{:keys [id on-success on-error]}]
   (firestore/delete-stock-item!
    id
    (fn [id] (when on-success (rf/dispatch (conj on-success id))))
    (fn [err] (when on-error (rf/dispatch (conj on-error err)))))))

(rf/reg-fx
 :firebase/fetch-stock-items
 (fn [{:keys [on-success on-error]}]
   (firestore/fetch-stock-items!
    (fn [items] (when on-success (rf/dispatch (conj on-success items))))
    (fn [err] (when on-error (rf/dispatch (conj on-error err)))))))

;; Needed item effects

(rf/reg-fx
 :firebase/add-needed-item
 (fn [{:keys [item on-success on-error]}]
   (firestore/add-needed-item!
    item
    (fn [item] (when on-success (rf/dispatch (conj on-success item))))
    (fn [err] (when on-error (rf/dispatch (conj on-error err)))))))

(rf/reg-fx
 :firebase/update-needed-item
 (fn [{:keys [item on-success on-error]}]
   (firestore/update-needed-item!
    item
    (fn [item] (when on-success (rf/dispatch (conj on-success item))))
    (fn [err] (when on-error (rf/dispatch (conj on-error err)))))))

(rf/reg-fx
 :firebase/delete-needed-item
 (fn [{:keys [id on-success on-error]}]
   (firestore/delete-needed-item!
    id
    (fn [id] (when on-success (rf/dispatch (conj on-success id))))
    (fn [err] (when on-error (rf/dispatch (conj on-error err)))))))

(rf/reg-fx
 :firebase/fetch-needed-items
 (fn [{:keys [on-success on-error]}]
   (firestore/fetch-needed-items!
    (fn [items] (when on-success (rf/dispatch (conj on-success items))))
    (fn [err] (when on-error (rf/dispatch (conj on-error err)))))))

(rf/reg-fx
 :firebase/delete-checked-needed-items
 (fn [{:keys [ids on-success on-error]}]
   (firestore/delete-checked-needed-items!
    ids
    (fn [ids] (when on-success (rf/dispatch (conj on-success ids))))
    (fn [err] (when on-error (rf/dispatch (conj on-error err)))))))
