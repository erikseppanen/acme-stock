(ns acme.app
  "Main app entry point"
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            ;; Register all events and subscriptions
            [acme.events.core]
            [acme.events.stock]
            [acme.events.needed]
            [acme.events.sync]
            [acme.subs.stock]
            [acme.subs.needed]
            [acme.fx.firebase]
            ;; Navigation
            [acme.native.navigation :as nav]))

(defn root-component []
  [nav/root-navigator])

;; Create a proper React component class for Expo
(def AppRoot (r/reactify-component root-component))

(defn ^:export init []
  ;; Initialize re-frame app-db
  (rf/dispatch-sync [:app/initialize])
  ;; Start Firebase listeners
  (rf/dispatch [:app/start-listeners])
  ;; Set the app component on global for index.js to pick up
  (set! js/global.acmeApp AppRoot))
