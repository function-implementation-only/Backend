FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/config-service-0.0.1-SNAPSHOT.jar config.jar
ENTRYPOINT ["java","-jar","config.jar"]
