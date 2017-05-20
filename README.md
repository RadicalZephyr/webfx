# WebFX

A configurable shim for running a web application in a local JavaFX
Webkit web view.

## Usage

Start your webserver, pass a configuration map (`Map<String, String>`)
to the static method `radicalzephyr.WebFX.start`.

Important options:

- `domain` - When combined with the port, determines the location of the
  page to show. Defaults to `localhost`
- `port` - When combined with the domain, determinse the location of
  the page to show. Defaults to `3000`
- `width` - The width of the JavaFX window. Defaults to `1024`
- `height` - The height of the JavaFX window. Defaults to `768`

## Requirements

Needs to be compiled _and_ run with Java 8 or later.

## License

Copyright © 2017 Geoff Shannon

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
