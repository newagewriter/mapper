#!/bin/bash
VERSION=$(grep -E -o "lib_version=(.*)" ./gradle.properties | cut -d '=' -f 2 | xargs)
echo "Current version = $VERSION"
if [ $VERSION == $1 ]; then
  echo "Version is valid"
  exit 0
else
  echo "Version not match"
  exit 1
fi