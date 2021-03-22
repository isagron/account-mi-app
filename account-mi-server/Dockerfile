FROM openjdk:15
ARG JAR_FILE_PATH=build/libs/account-mi-server-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE_PATH} app.jar
ENTRYPOINT ["java", "-jar","/app.jar"]
