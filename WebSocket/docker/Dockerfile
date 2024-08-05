FROM openjdk:17

ARG JAR_FILE=*.jar

COPY docker/${JAR_FILE} application.jar

ENTRYPOINT ["java", "-jar", "application.jar"]

EXPOSE 8081