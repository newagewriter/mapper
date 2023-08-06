#!/bin/bash
VERSION=$(grep -E -o "lib_version=(.*)" ./gradle.properties | cut -d '=' -f 2 | xargs)
RELEASE_VERSION=$(echo "$1" | cut -d '/' -f 2 | xargs)
echo "Current version = $VERSION"
echo "Release version = $RELEASE_VERSION"
if [ "$VERSION" == "$RELEASE_VERSION" ]; then
  echo "Version is valid"
  exit 0
else
  echo "Version not match"
  exit 1
fi