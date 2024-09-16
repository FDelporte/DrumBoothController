#! /usr/bin/bash
java \
  -Dglass.platform=gtk \
  -Djava.library.path=/opt/javafx-sdk/lib \
  -Dmonocle.platform.traceConfig=false \
  -Dprism.verbose=false \
  -Djavafx.verbose=false \
  --module-path .:/opt/javafx-sdk/lib \
  --add-modules javafx.controls \
  -jar drumbooth-0.0.2.jar $@
