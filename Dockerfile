# For Java 8, try this
# FROM openjdk:8-jdk-alpine

# For Java 11, try this
FROM adoptopenjdk/openjdk11:alpine-jre

#FROM adoptopenjdk:11-jre-hotspot

# Refer to Maven build -> finalName
ARG JAR_FILE=target/agrimart-api-1.0.jar

# cd /opt/app
WORKDIR /opt/app

# cp target/hastin-1.0.jar /opt/app/app.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8080

# java -jar /opt/app/app.jar
ENTRYPOINT ["java","-jar","app.jar"]
 