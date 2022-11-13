FROM openjdk:17-jdk-alpine
COPY target/*.jar questionnaire.jar
ENTRYPOINT ["java","-jar", "questionnaire.jar"]