FROM  openjdk:15-jdk-alpine

COPY target/server-api-0.0.1-SNAPSHOT.jar server-mangaclawers.jar

EXPOSE 4000

ENTRYPOINT ["java", "-jar", "server-mangaclawers.jar"]
