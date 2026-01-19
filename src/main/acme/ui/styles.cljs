(ns acme.ui.styles
  "Shared styles for the app"
  (:require ["react-native" :refer [StyleSheet]]))

;; Color palette
(def colors
  {:primary "#4A90D9"
   :primary-dark "#3A7BC8"
   :secondary "#6C757D"
   :success "#28A745"
   :danger "#DC3545"
   :warning "#FFC107"
   :light "#F8F9FA"
   :dark "#343A40"
   :white "#FFFFFF"
   :black "#000000"
   :gray-100 "#F8F9FA"
   :gray-200 "#E9ECEF"
   :gray-300 "#DEE2E6"
   :gray-400 "#CED4DA"
   :gray-500 "#ADB5BD"
   :gray-600 "#6C757D"
   :border "#E0E0E0"
   :background "#F5F5F5"
   :card-background "#FFFFFF"
   :text-primary "#212529"
   :text-secondary "#6C757D"
   :text-muted "#ADB5BD"})

;; Spacing scale
(def spacing
  {:xs 4
   :sm 8
   :md 16
   :lg 24
   :xl 32})

;; Typography
(def typography
  {:h1 {:fontSize 28 :fontWeight "bold"}
   :h2 {:fontSize 24 :fontWeight "bold"}
   :h3 {:fontSize 20 :fontWeight "600"}
   :body {:fontSize 16}
   :small {:fontSize 14}
   :caption {:fontSize 12}})

;; Common styles
(def common-styles
  (.create StyleSheet
           #js {:container #js {:flex 1
                                :backgroundColor (:background colors)}
                :safeArea #js {:flex 1
                               :backgroundColor (:background colors)}
                :screen #js {:flex 1
                             :padding (:md spacing)}
                :card #js {:backgroundColor (:card-background colors)
                           :borderRadius 12
                           :padding (:md spacing)
                           :marginBottom (:sm spacing)
                           :boxShadow "0px 2px 4px rgba(0, 0, 0, 0.1)"
                           :elevation 3}
                :row #js {:flexDirection "row"
                          :alignItems "center"}
                :spaceBetween #js {:flexDirection "row"
                                   :alignItems "center"
                                   :justifyContent "space-between"}
                :center #js {:alignItems "center"
                             :justifyContent "center"}
                :textInput #js {:backgroundColor (:white colors)
                                :borderWidth 1
                                :borderColor (:border colors)
                                :borderRadius 8
                                :padding (:sm spacing)
                                :fontSize 16}
                :button #js {:backgroundColor (:primary colors)
                             :paddingVertical (:sm spacing)
                             :paddingHorizontal (:md spacing)
                             :borderRadius 8
                             :alignItems "center"
                             :justifyContent "center"}
                :buttonText #js {:color (:white colors)
                                 :fontSize 16
                                 :fontWeight "600"}
                :buttonSecondary #js {:backgroundColor (:gray-200 colors)
                                      :paddingVertical (:sm spacing)
                                      :paddingHorizontal (:md spacing)
                                      :borderRadius 8
                                      :alignItems "center"
                                      :justifyContent "center"}
                :buttonSecondaryText #js {:color (:text-primary colors)
                                          :fontSize 16
                                          :fontWeight "600"}
                :buttonDanger #js {:backgroundColor (:danger colors)
                                   :paddingVertical (:sm spacing)
                                   :paddingHorizontal (:md spacing)
                                   :borderRadius 8
                                   :alignItems "center"
                                   :justifyContent "center"}
                :title #js {:fontSize 28
                            :fontWeight "bold"
                            :color (:text-primary colors)
                            :marginBottom (:md spacing)}
                :subtitle #js {:fontSize 18
                               :fontWeight "600"
                               :color (:text-primary colors)}
                :bodyText #js {:fontSize 16
                               :color (:text-primary colors)}
                :mutedText #js {:fontSize 14
                                :color (:text-muted colors)}
                :fab #js {:position "absolute"
                          :right (:md spacing)
                          :bottom (:md spacing)
                          :width 56
                          :height 56
                          :borderRadius 28
                          :backgroundColor (:primary colors)
                          :alignItems "center"
                          :justifyContent "center"
                          :boxShadow "0px 4px 8px rgba(0, 0, 0, 0.3)"
                          :elevation 8}
                :fabText #js {:color (:white colors)
                              :fontSize 28
                              :fontWeight "bold"}
                :emptyState #js {:flex 1
                                 :alignItems "center"
                                 :justifyContent "center"
                                 :padding (:xl spacing)}
                :emptyStateText #js {:fontSize 16
                                     :color (:text-muted colors)
                                     :textAlign "center"}}))

(defn get-style [key]
  (aget common-styles (name key)))
