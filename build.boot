(def project 'radicalzephyr/webfx)
(def version "0.1.0-SNAPSHOT")

(set-env! :resource-paths #{"src"}
          :dependencies [])

(task-options!
 pom {:project     project
      :version     version
      :description "A configurable shim for running a web application in a local JavaFX Webkit web view."
      :url         "https://github.com/RadicalZephyr/webfx"
      :scm         {:url "https://github.com/RadicalZephyr/webfx"}
      :license     {"Eclipse Public License"
                    "http://www.eclipse.org/legal/epl-v10.html"}})

(deftask build
  "Build and install the project locally."
  []
  (comp (javac) (pom) (jar) (install)))
