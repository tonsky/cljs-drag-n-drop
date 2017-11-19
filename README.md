# Sane wrapper around Drag-n-Drop DOM API

## Highlights

- Only supports files. No in-window (e.g. text) drag-n-drop
- Guarantees order of events
- Simulates `dragstart`/`dragend` events
- Multiple listeners at once
- Cross-browser support

## Usage

Include:

```clojure
[cljs-drag-n-drop "0.1.0"]
```

Require:

```clojure
(require [cljs-drag-n-drop.core :as dnd])
```

Use:

```clojure
(dnd/subscribe! (js/document.querySelector "div") :unique-key
  { :start (fn [e]       (println "d1 start"))
    :enter (fn [e]       (println "d1 enter"))
    :drop  (fn [e files] (println "d1 drop"))
    :leave (fn [e]       (println "d1 leave"))
    :end   (fn [e]       (println "d1 end")) })

...

(dnd/unsubscribe! (js/document.querySelector "div") :unique-key)
```

To catch all events, subscribe to `js/document.documentElement`.

## Simulated events

Cljs-drag-n-drop offers a sane and reliable event model to work with.

Events always fire in that order:

1. `start`. Every time file is dragged on top of **browser window**. It means it might not be on top of your element yet, but `start` is fired anyway to indicate dragging has started.
2. `enter`. When file is dragged **over specified element**.
3. `drop`. Drop is only fired if file was released (dropped) on top of specified element or any of its children.
4. `leave`. Leave is guaranteed to fire if `enter` was firer before. It’s raised when:
  - mouse has left specified element
  - drop happened
  - drag was cancelled (e.g. via Esc button)
5. `end`. End is guaranteed to fire if `enter` was fired before. It’s raised if
  - successful drop happened 
  - drag was cancelled (e.g. via Esc button)

## Developing

Run dev environment:

```
lein fighweel
```

Connect to http://localhost:8080

## License

Copyright © 2017 Nikita Prokopov

Licensed under Eclipse Public License (see [LICENSE](LICENSE)).