#!/bin/sh

curl -v  --header "SOAPAction: \"http://localhost:8008/Save\"" \
     --header "content-type: text/soap+xml; charset=utf-8" \
      --data @save.xml \
      -X POST http://localhost:8088/foo

