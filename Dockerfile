FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/SpeedSideProject-0.0.1-SNAPSHOT.jar ssp.jar
ENTRYPOINT ["java","-jar","ssp.jar"]
