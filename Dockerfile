# Specify a base image
FROM openjdk:17

# Copy the jar file into the Docker image
COPY target/crypto-0.1-SNAPSHOT.jar /usr/app/crypto-0.1-SNAPSHOT.jar
COPY src/main/resources/application.properties /application.properties

# Specify the command to run
ENTRYPOINT java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 -jar /usr/app/crypto-0.1-SNAPSHOT.jar

EXPOSE 8080 5005