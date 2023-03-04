FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/discoveryservice-0.0.1-SNAPSHOT.jar discovery.jar
ENTRYPOINT ["java","-jar","discovery.jar"]
