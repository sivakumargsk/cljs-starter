(ns cljs-starter.app-test
  (:require-macros [cljs.test :refer [deftest testing is]])
  (:require [cljs.test :as t]
            [cljs-starter.app :as app]))

(deftest test-arithmetic []
  (is (= 1 0)))
