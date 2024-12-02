FROM ubuntu:latest
LABEL authors="yash"
VOLUME /tmp
#FROM openjdk:17-jdk-slim
EXPOSE 8080
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar","app.jar"]