FROM openjdk:17-jdk-alpine
COPY target/*.jar questionnaire.jar
ENTRYPOINT ["java","-jar", "questionnaire.jar"]

#FROM openjdk:17-jdk-alpine
#ADD target/*.jar questionnaire.jar
#EXPOSE 8080
#ENTRYPOINT ["java","-jar", "questionnaire.jar"]