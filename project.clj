(defproject cn.leancloud/clj.qiniu "0.2.2"
  :description "Clojure SDK for qiniu.com storage."
  :url "https://github.com/leancloud/clj.qiniu"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clj-http "3.9.0"]
                 [cheshire "5.8.0"]
                 [com.qiniu/qiniu-java-sdk "7.2.12"]
                 [com.github.ben-manes.caffeine/caffeine "2.6.0"]])
