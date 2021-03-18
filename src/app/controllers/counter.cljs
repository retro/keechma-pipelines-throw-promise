(ns app.controllers.counter
  (:require [keechma.next.controller :as ctrl]
            [keechma.next.controllers.pipelines :as pipelines]
            [keechma.pipelines.core :as pp :refer-macros [pipeline!]]
            [promesa.core :as p]))

(derive :counter ::pipelines/controller)

(def pipelines
  {:inc-1 (pipeline! [value {:keys [state*]}]
            (p/delay 1000)
            (swap! state* update :inc-1 inc))
   :inc-2 (pipeline! [value {:keys [state*]}]
            (p/delay 1000)
            (swap! state* update :inc-2 inc))})

(defmethod ctrl/prep :counter [ctrl] (pipelines/register ctrl pipelines))

(defmethod ctrl/start :counter [_ _ _]
  {:inc-1 0
   :inc-2 0})