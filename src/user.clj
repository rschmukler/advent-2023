(ns user
  (:require [portal.api :as p]))

(defonce portal
  (p/open {:theme :portal.colors/nord}))

(add-tap #'p/submit)
