#!/usr/bin/env bash
export ENABLE_GLUON_COMMERCIAL_EXTENSIONS=true
java \
  -Degl.displayid=/dev/dri/card0 \
  -Dmonocle.egl.lib=/opt/javafx/lib/libgluon_drm-1.1.3.so \
  -Djava.library.path=/opt/javafx-sdk/lib \
  -Dmonocle.platform.traceConfig=false \
  -Dprism.verbose=false \
  -Djavafx.verbose=false \
  -Dmonocle.platform=EGL \
  --module-path .:/opt/javafx-sdk/lib \
  --add-modules javafx.controls \
  -jar drumbooth-0.0.2.jar $@
