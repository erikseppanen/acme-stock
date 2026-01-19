(ns acme.firebase.listeners
  "Real-time Firestore listeners"
  (:require [acme.firebase.config :as config]
            [acme.firebase.firestore :as firestore]
            [re-frame.core :as rf]
            ["firebase/firestore" :refer [collection onSnapshot query orderBy]]))

(defonce unsubscribe-fns (atom {}))

(defn- doc->clj
  "Convert Firestore document to Clojure map"
  [doc-snapshot]
  (when (.-exists doc-snapshot)
    (let [data (js->clj (.data doc-snapshot) :keywordize-keys true)]
      (assoc data :id (.-id doc-snapshot)))))

(defn subscribe-stock-items!
  "Subscribe to real-time stock items updates"
  []
  (let [db (config/get-db)
        coll (collection db firestore/STOCK-COLLECTION)
        q (query coll (orderBy "created-at" "desc"))
        unsubscribe (onSnapshot q
                                (fn [snapshot]
                                  (let [items (->> (.-docs snapshot)
                                                   (map doc->clj)
                                                   (filter some?))]
                                    (rf/dispatch [:sync/stock-items-received items])))
                                (fn [error]
                                  (js/console.error "Stock listener error:" error)))]
    (swap! unsubscribe-fns assoc :stock unsubscribe)
    unsubscribe))

(defn subscribe-needed-items!
  "Subscribe to real-time needed items updates"
  []
  (let [db (config/get-db)
        coll (collection db firestore/NEEDED-COLLECTION)
        q (query coll (orderBy "created-at" "desc"))
        unsubscribe (onSnapshot q
                                (fn [snapshot]
                                  (let [items (->> (.-docs snapshot)
                                                   (map doc->clj)
                                                   (filter some?))]
                                    (rf/dispatch [:sync/needed-items-received items])))
                                (fn [error]
                                  (js/console.error "Needed listener error:" error)))]
    (swap! unsubscribe-fns assoc :needed unsubscribe)
    unsubscribe))

(defn unsubscribe-all!
  "Unsubscribe from all listeners"
  []
  (doseq [[_ unsubscribe] @unsubscribe-fns]
    (when unsubscribe (unsubscribe)))
  (reset! unsubscribe-fns {}))

(defn subscribe-all!
  "Subscribe to all collections"
  []
  (subscribe-stock-items!)
  (subscribe-needed-items!))
