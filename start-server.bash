#! /bin/bash

docker run -d --name mongo mongo
docker run -d --name kafka -p 9092:9092 apache/kafka:3.8.0
bash ./gradlew bootRun --stacktrace
docker stop mongo kafka
docker rm mongo kafka
yes | docker system prune