#!/bin/sh
cd ./ && ./mvnw clean package
java -jar ./target/*-0.0.1-SNAPSHOT.jar
#debug模式
#java -Xdebug -Xrunjdwp:transport=dt_socket,address=5005,server=y,suspend=y -jar ./target/*-0.0.1-SNAPSHOT.jar