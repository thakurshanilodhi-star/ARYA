#!/bin/sh
set -eu
APP_HOME=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
DIST="$HOME/.gradle/wrapper/dists/gradle-8.7-bin"
ZIP="$HOME/.gradle/wrapper/gradle-8.7-bin.zip"
if [ ! -x "$HOME/.gradle/aria-gradle-8.7/bin/gradle" ]; then
  mkdir -p "$HOME/.gradle"
  if [ ! -f "$ZIP" ]; then
    wget -O "$ZIP" "https://services.gradle.org/distributions/gradle-8.7-bin.zip"
  fi
  rm -rf "$HOME/.gradle/aria-gradle-8.7.tmp"
  mkdir -p "$HOME/.gradle/aria-gradle-8.7.tmp"
  unzip -q "$ZIP" -d "$HOME/.gradle/aria-gradle-8.7.tmp"
  rm -rf "$HOME/.gradle/aria-gradle-8.7"
  mv "$HOME/.gradle/aria-gradle-8.7.tmp/gradle-8.7" "$HOME/.gradle/aria-gradle-8.7"
fi
exec "$HOME/.gradle/aria-gradle-8.7/bin/gradle" -p "$APP_HOME" "$@"
