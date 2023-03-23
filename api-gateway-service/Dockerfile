FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/api-gate-0.0.1-SNAPSHOT.jar apigate.jar
ENTRYPOINT ["java","-jar","apigate.jar"]
