FROM ubuntu:latest
ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update && apt-get install -y tzdata

FROM openjdk:17-alpine
RUN addgroup -S spring && adduser -S spring -G spring
MAINTAINER Bruno Pastorelli
COPY target/visitas-ms.jar visitas-ms.jar
ENTRYPOINT ["java","-jar","/visitas-ms.jar"]
EXPOSE 9096