(ns ui.http
  (:require
   [ui.db :as db]
   [clojure.string :as s]
   [taoensso.encore :refer [url-encode]]))



(defn make-res-url [{:keys [filename dir]}]
  (assert (or
           (s/starts-with? dir (:internal-root @db/sys-config-store))
           (s/starts-with? dir (:sd-root @db/sys-config-store))))
  ;;(str api-server "/res?dir=" (url-encode dir) "&filename=" (url-encode filename))
  (str (:api-server @db/sys-config-store) (subs dir 4) "/" filename))
