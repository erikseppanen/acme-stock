(ns acme.firebase.firestore
  "Firestore CRUD operations"
  (:require [acme.firebase.config :as config]
            ["firebase/firestore" :refer [collection doc getDoc getDocs
                                          setDoc updateDoc deleteDoc
                                          query orderBy serverTimestamp]]))

;; Collection names
(def STOCK-COLLECTION "stock-items")
(def NEEDED-COLLECTION "needed-items")

(defn- doc->clj
  "Convert Firestore document to Clojure map"
  [doc-snapshot]
  (when (.-exists doc-snapshot)
    (let [data (js->clj (.data doc-snapshot) :keywordize-keys true)]
      (assoc data :id (.-id doc-snapshot)))))

(defn- clj->doc
  "Convert Clojure map to Firestore document data"
  [item]
  (-> item
      (dissoc :id)
      (clj->js)))

;; Stock items operations

(defn add-stock-item!
  "Add a new stock item to Firestore"
  [item on-success on-error]
  (let [db (config/get-db)
        coll (collection db STOCK-COLLECTION)
        doc-ref (doc coll (:id item))]
    (-> (setDoc doc-ref (clj->doc item))
        (.then #(on-success item))
        (.catch on-error))))

(defn update-stock-item!
  "Update an existing stock item"
  [item on-success on-error]
  (let [db (config/get-db)
        doc-ref (doc db STOCK-COLLECTION (:id item))]
    (-> (updateDoc doc-ref (clj->doc item))
        (.then #(on-success item))
        (.catch on-error))))

(defn delete-stock-item!
  "Delete a stock item"
  [id on-success on-error]
  (let [db (config/get-db)
        doc-ref (doc db STOCK-COLLECTION id)]
    (-> (deleteDoc doc-ref)
        (.then #(on-success id))
        (.catch on-error))))

(defn fetch-stock-items!
  "Fetch all stock items"
  [on-success on-error]
  (let [db (config/get-db)
        coll (collection db STOCK-COLLECTION)
        q (query coll (orderBy "created-at" "desc"))]
    (-> (getDocs q)
        (.then (fn [snapshot]
                 (let [items (->> (.-docs snapshot)
                                  (map doc->clj)
                                  (filter some?))]
                   (on-success items))))
        (.catch on-error))))

;; Needed items operations

(defn add-needed-item!
  "Add a new needed item to Firestore"
  [item on-success on-error]
  (let [db (config/get-db)
        coll (collection db NEEDED-COLLECTION)
        doc-ref (doc coll (:id item))]
    (-> (setDoc doc-ref (clj->doc item))
        (.then #(on-success item))
        (.catch on-error))))

(defn update-needed-item!
  "Update an existing needed item"
  [item on-success on-error]
  (let [db (config/get-db)
        doc-ref (doc db NEEDED-COLLECTION (:id item))]
    (-> (updateDoc doc-ref (clj->doc item))
        (.then #(on-success item))
        (.catch on-error))))

(defn delete-needed-item!
  "Delete a needed item"
  [id on-success on-error]
  (let [db (config/get-db)
        doc-ref (doc db NEEDED-COLLECTION id)]
    (-> (deleteDoc doc-ref)
        (.then #(on-success id))
        (.catch on-error))))

(defn fetch-needed-items!
  "Fetch all needed items"
  [on-success on-error]
  (let [db (config/get-db)
        coll (collection db NEEDED-COLLECTION)
        q (query coll (orderBy "created-at" "desc"))]
    (-> (getDocs q)
        (.then (fn [snapshot]
                 (let [items (->> (.-docs snapshot)
                                  (map doc->clj)
                                  (filter some?))]
                   (on-success items))))
        (.catch on-error))))

(defn delete-checked-needed-items!
  "Delete all checked needed items"
  [checked-ids on-success on-error]
  (let [db (config/get-db)
        promises (map (fn [id]
                        (deleteDoc (doc db NEEDED-COLLECTION id)))
                      checked-ids)]
    (-> (js/Promise.all (clj->js promises))
        (.then #(on-success checked-ids))
        (.catch on-error))))
