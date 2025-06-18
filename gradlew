#!/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
java -jar "$DIR/gradle/wrapper/gradle-wrapper.jar" "$@"
