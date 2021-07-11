
# Build jar file
FROM maven:3.8.1-openjdk-15 AS build
COPY src /server-mangaclawers/src
COPY pom.xml /server-mangaclawers/
RUN mvn package -f /server-mangaclawers/pom.xml -DPORT=4000 -DPOSTGRES_URI=postgresql://ec2-3-214-136-47.compute-1.amazonaws.com:5432/da85j6njcdisla -DPOSTGRES_PASSWORD=eef0d359f7afaf23d90d3a75f11626e61bcb5c2cf505d65de87416cbfb33eed1 -DPOSTGRES_USERNAME=gxuumymkvzuzpq -DCLIENT_POINT_CHANGE_PASS=http://localhost:3000/auth/changepassword -DCLIENT_POINT_VERIFY_ACCOUNT=http://localhost:3000/auth/verification -DORIGIN_CLIENT=http://localhost:3000 -DCLOUDINARY_API_KEY=341627665577959 -DCLOUDINARY_API_SECRET=n26EJWYnUu9_0iRHAaXK0aiQIIE -DGOOGLE_CLIENT_ID=264258136890-avt4brhbqbvin83pare4umd3inv49s2t.apps.googleusercontent.com -DGOOGLE_CLIENT_SECRET=yQnWqXKo3SqDbxkZk-VCuAMm -DJWT_KEY=thisisauthenticationtokensecretkeyformangaclawerswebsite -DEMAIL_USERNAME=mangacrawlers123@gmail.com -DEMAIL_PASSWORD=MangaManga123


# run jar file
FROM openjdk:15-jdk-alpine
COPY --from=build /server-mangaclawers/target/server-api-0.0.1-SNAPSHOT.jar server-mangaclawers.jar
EXPOSE 4000
ENTRYPOINT ["java", "-jar", "server-mangaclawers.jar"]


# If want to build and run only this Dockerfile instead of docker compose, add local.env file when run >>> follow below
## docker build -t server-mangaclawers .
## docker run --rm -it --env-file src/local.env server-mangaclawers:latest