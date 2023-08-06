#!/bin/bash
LAST_VERSION=$(grep -E -o "# $1" ./CHANGELOG.md)
echo "Current version = $LAST_VERSION"
if [ -z "$LAST_VERSION" ]; then
  echo "Version not match"
  exit 1
else
  echo "Version is valid"
  exit 0
fi