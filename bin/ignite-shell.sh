#!/usr/bin/env bash

SCALA_VERSION=2.12

CP=target/scala-$SCALA_VERSION/ignite-shell-assembly-0.1.jar

java -classpath "$CP" org.apache.ignite.shell.Main $@
