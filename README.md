# management

The hosting release wrapper is faciliting the heroku method of releasing from a github repository
and using a private jar repository to pull the application component jars in from. It ads no
functionality of its own. Serves as place to run all the component acceptence tests on the management interface

## Prerequisites

You will need [Leiningen][1] 2 or above installed.

[1]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein ring server

## License

Copyright © 2013 Matt Burns
