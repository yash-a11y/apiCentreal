#FROM ubuntu:latest
FROM openjdk:17-jdk-slim
LABEL authors="yash"
VOLUME /tmp

EXPOSE 8080
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar","app.jar"]