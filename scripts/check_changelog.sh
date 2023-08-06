#!/bin/bash
RELEASE_VERSION=$(echo "$1" | cut -d '/' -f 2 | xargs)
LAST_VERSION=$(grep -E -o "# $RELEASE_VERSION" ./CHANGELOG.md)
echo "Current version = $LAST_VERSION"
if [ -z "$LAST_VERSION" ]; then
  echo "Missing release note in CHANGELOG.md file"
  exit 1
else
  echo "Found release note for version: $LAST_VERSION"
  exit 0
fi