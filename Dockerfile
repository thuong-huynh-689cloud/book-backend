FROM maven:3.8.7-eclipse-temurin-8-alpine AS build
ARG profile=dev
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -P${profile}

FROM ubuntu:20.04

RUN apt-get update
RUN apt-get install sudo
RUN sudo apt-get install nano
RUN sudo apt install ffmpeg -y
RUN sudo apt-get install -y openjdk-8-jdk

# Setup JAVA_HOME, this is useful for docker commandline
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64/

# Refer to Maven build -> finalName
#ARG JAR_FILE=target/secure-streaming-backend-1.0-SNAPSHOT.jar

# cd /opt/app
WORKDIR /opt/app

# cp target/spring-boot-web.jar /opt/app/app.jar
#COPY ${JAR_FILE} app.jar
#RUN ls -la *
COPY --from=build /home/app/target/secure-streaming-backend-1.0-SNAPSHOT.jar app.jar

EXPOSE 8080

# java -jar /opt/app/app.jar
CMD ["java","-server","-XX:+UseContainerSupport","-XX:+HeapDumpOnOutOfMemoryError","-jar","app.jar"]
