FROM openjdk:17-alpine
COPY ./target/Test-Server-0.1.jar /usr/app/
WORKDIR /usr/app
EXPOSE 7777
ENTRYPOINT ["java", "-jar", "Test-Server-0.1.jar"]
