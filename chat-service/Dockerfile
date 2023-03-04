FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/chat-service-0.0.1-SNAPSHOT.jar chat.jar
ENTRYPOINT ["java","-jar","chat.jar"]
