#  docker build -t image-db-backend .

FROM openjdk:21-jdk-slim

RUN addgroup --system spring && adduser --system spring --ingroup spring

USER spring:spring

WORKDIR /app

COPY build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]