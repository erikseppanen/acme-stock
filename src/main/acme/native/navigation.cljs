(ns acme.native.navigation
  "React Navigation setup with bottom tabs"
  (:require [reagent.core :as r]
            ["react" :as react]
            ["@react-navigation/native" :refer [NavigationContainer useNavigation]]
            ["@react-navigation/bottom-tabs" :refer [createBottomTabNavigator]]
            ["react-native" :refer [Text View]]
            ["react-native-safe-area-context" :refer [SafeAreaProvider]]
            [acme.ui.screens.stock-list :as stock-list]
            [acme.ui.screens.needed-list :as needed-list]
            [acme.ui.screens.item-detail :as item-detail]
            [acme.ui.styles :as styles]))

;; Create navigators
(def Tab (createBottomTabNavigator))

;; Screen wrapper that properly uses the useNavigation hook
(defn make-screen [screen-component]
  (fn [^js props]
    (let [navigation (useNavigation)]
      (r/as-element
       [screen-component {:navigation navigation
                          :route (.-route props)}]))))

;; Create screen components
(def StockScreen (make-screen stock-list/stock-list-screen))
(def NeededScreen (make-screen needed-list/needed-list-screen))
(def DetailScreen (make-screen item-detail/item-detail-screen))

;; Tab bar icons - these need to be functions that return React elements
(defn stock-icon [^js props]
  (r/as-element
   [:> Text {:style #js {:fontSize (.-size props) :color (.-color props)}}
    "📦"]))

(defn needed-icon [^js props]
  (r/as-element
   [:> Text {:style #js {:fontSize (.-size props) :color (.-color props)}}
    "🛒"]))

;; Main tab navigator
(defn main-tabs []
  [:> (.-Navigator Tab)
   {:screenOptions {:tabBarActiveTintColor (:primary styles/colors)
                    :tabBarInactiveTintColor (:text-muted styles/colors)
                    :headerShown false}}
   [:> (.-Screen Tab)
    {:name "Stock"
     :component StockScreen
     :options {:tabBarIcon stock-icon
               :tabBarLabel "Stock"}}]
   [:> (.-Screen Tab)
    {:name "Needed"
     :component NeededScreen
     :options {:tabBarIcon needed-icon
               :tabBarLabel "Shopping"}}]
   [:> (.-Screen Tab)
    {:name "ItemDetail"
     :component DetailScreen
     :options {:tabBarButton (fn [] nil)}}]])

;; Root navigator
(defn root-navigator []
  [:> SafeAreaProvider
   [:> NavigationContainer
    [main-tabs]]])
