(ns acme.events.core
  "Core app lifecycle events"
  (:require [re-frame.core :as rf]
            [acme.db.core :as db]))

;; Initialize app-db
(rf/reg-event-fx
 :app/initialize
 (fn [_ _]
   {:db db/default-db
    :firebase/init true}))

;; Start real-time listeners after Firebase is ready
(rf/reg-event-fx
 :app/start-listeners
 (fn [{:keys [db]} _]
   {:db (assoc-in db [:ui :loading?] true)
    :firebase/subscribe true}))

;; Stop real-time listeners
(rf/reg-event-fx
 :app/stop-listeners
 (fn [_ _]
   {:firebase/unsubscribe true}))

;; UI navigation
(rf/reg-event-db
 :ui/set-screen
 (fn [db [_ screen]]
   (assoc-in db [:ui :current-screen] screen)))

;; Set editing item
(rf/reg-event-db
 :ui/set-editing-item
 (fn [db [_ item-id]]
   (assoc-in db [:ui :editing-item] item-id)))

;; Clear editing item
(rf/reg-event-db
 :ui/clear-editing-item
 (fn [db _]
   (assoc-in db [:ui :editing-item] nil)))

;; Set search query
(rf/reg-event-db
 :ui/set-search-query
 (fn [db [_ query]]
   (assoc-in db [:ui :search-query] query)))

;; Set loading state
(rf/reg-event-db
 :ui/set-loading
 (fn [db [_ loading?]]
   (assoc-in db [:ui :loading?] loading?)))

;; Handle errors
(rf/reg-event-fx
 :app/error
 (fn [{:keys [db]} [_ error]]
   (js/console.error "App error:" error)
   {:db (assoc-in db [:ui :loading?] false)}))
