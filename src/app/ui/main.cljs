(ns app.ui.main
  (:require [keechma.next.helix.core :refer [with-keechma use-sub use-meta-sub dispatch]]
            [keechma.next.helix.lib :refer [defnc]]
            [helix.hooks :refer [use-callback]]
            [helix.core :as hx :refer [$ suspense]]
            [helix.dom :as d]
            [app.ui.pages.home :refer [Home]]

            [keechma.next.controllers.pipelines :refer [throw-promise! get-promise]]))

(defnc Counter [{:keys [action] :as props}]
  (let [promise-getter (use-callback
                         [action]
                         (fn [meta-state]
                           (get-promise meta-state action)))
        val-getter (use-callback
                     [action]
                     (fn [state]
                       (get state action)))
        val (use-sub props :counter val-getter)
        promise (use-meta-sub props :counter promise-getter)]
    (when promise
      (throw promise))
    (d/div
      {:class "border bd-black"}
      (d/div
        {:class "py-2"}
        "Result: " val)
      (d/button
        {:onClick #(dispatch props :counter action)}
        "Increment value"))))

(defnc MainRenderer [props]
  (d/div
    {:class "p-4"}
    (suspense
      {:fallback (d/div "Loading")}
      ($ Counter {:action :inc-1 & props}))
    (suspense
      {:fallback (d/div "Loading")}
      ($ Counter {:action :inc-2 & props}))))

(def Main (with-keechma MainRenderer))