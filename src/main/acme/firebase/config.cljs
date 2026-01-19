(ns acme.firebase.config
  "Firebase initialization and configuration"
  (:require ["firebase/app" :refer [initializeApp]]
            ["firebase/firestore" :refer [getFirestore]]
            ["react-native" :refer [Platform]]))

;; Firebase configuration - replace with your own config
(def firebase-config
  #js {:apiKey "AIzaSyDAXrIaMj1UrP6BNzMp-gz4RZWbf1IKUKM"
       :authDomain "acme-stock.firebaseapp.com"
       :projectId "acme-stock"
       :storageBucket "acme-stock.firebasestorage.app"
       :messagingSenderId "458586428936"
       :appId "1:458586428936:web:76b4d390797b6f6fad63bd"})

(defonce app (atom nil))
(defonce db (atom nil))

(defn init-firebase!
  "Initialize Firebase app and Firestore"
  []
  (when-not @app
    (reset! app (initializeApp firebase-config))
    ;; Use default Firestore - it handles caching automatically per platform
    (reset! db (getFirestore @app))))

(defn get-db
  "Get Firestore database instance"
  []
  @db)
