#!/bin/sh
#export VERSION;
./mvnw clean
docker build -t school-service-es:v0.0.1 -f ./Dockerfile .