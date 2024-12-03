#FROM ubuntu:latest
#FROM openjdk:17-jdk-slim
#LABEL authors="yash"
#VOLUME /tmp
#
#EXPOSE 8080
#COPY target/*.jar app.jar
#ENTRYPOINT ["java", "-jar","app.jar"]
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]