#!/bin/bash

RESULT=$(grep -n "# [0-9]." ./CHANGELOG.md | cut -d : -f 1)
array=($RESULT)
start_line=${array[0]}
end_line=$((array[1] - 1))

sed -n "${start_line},${end_line}p" ./CHANGELOG.md | tee tmp_release_note.txt