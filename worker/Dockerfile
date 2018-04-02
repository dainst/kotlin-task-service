FROM openjdk:8-jdk-alpine
ADD build/libs/worker.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]