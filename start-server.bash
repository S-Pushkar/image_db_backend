#! /bin/bash

#docker run -d --name mongo mongo
#export MONGODB_URI="$(docker inspect --format='{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' mongo):27017"
docker run -d --name kafka -p 9092:9092 apache/kafka:3.8.0
docker run -d --name front -p 3000:3000 image-db-frontend
bash ./gradlew bootRun --stacktrace
#docker stop mongo kafka front
#docker rm mongo kafka front
docker stop kafka front
docker rm kafka front
yes | docker system prune