
# Build jar file
FROM maven:3.8.1-openjdk-15 AS build
COPY src /server-mangaclawers/src
COPY pom.xml /server-mangaclawers/
RUN mvn package -f /server-mangaclawers/pom.xml 
# RUN mvn package -f /server-mangaclawers/pom.xml -DPOSTGRES_PASSWORD=0b889a55344e9cd0495a869c7261bf4380136e75b26b91e1aaf0776340f78ce7 -DPOSTGRES_USERNAME=kopgjqgyhmvdac -DPOSTGRES_URI=postgresql://ec2-54-166-37-125.compute-1.amazonaws.com:5432/d1k37im2oead9c -DDATABASE_URL=postgres://kopgjqgyhmvdac:e6ea5999bb08c19a4948770c0aeb1798c0f6c40e7e4df51203389b95973b4d18@ec2-54-166-37-125.compute-1.amazonaws.com:5432/d1k37im2oead9c -DCLIENT_POINT_CHANGE_PASS=https://www.mangacrawlers.tk/auth/changepassword -DCLIENT_POINT_VERIFY_ACCOUNT=https://www.mangacrawlers.tk/auth/verification -DORIGIN_PRODUCTION01=https://www.mangacrawlers.tk/ -DHOST_PRODUCTION=https://mangacrawlers-server-production.up.railway.app/ -DHOST_PRODUCTION02=https://api.mangacrawlers.tk/ -DCLOUDINARY_API_KEY=341627665577959 -DGOOGLE_CLIENT_ID=264258136890-avt4brhbqbvin83pare4umd3inv49s2t.apps.googleusercontent.com -DGOOGLE_CLIENT_SECRET=yQnWqXKo3SqDbxkZk-VCuAMm -DYOUTUBE_API_KEY=AIzaSyCvlMIC_CCzzwfC2RrUhwEb21yDtLykn6c -DJWT_KEY=3A103B744F4FABD8C09CA9A8894F0F0E3775CF5E803232CBC57153D39E296C691F9210F794032549A77A07FBF74E9A8E04AF41B1808EC64BB182EEA3ABEBFD89 -DEMAIL_USERNAME=mangacrawlers123@gmail.com -DEMAIL_PASSWORD=MangaManga123
# RUN mvn package -f /server-mangaclawers/pom.xml -Denv01=val01 -Denv02=val02  >>  add env variable


# run jar file
FROM openjdk:15-jdk-alpine
COPY --from=build /server-mangaclawers/target/server-api-0.0.1-SNAPSHOT.jar server-mangaclawers.jar
EXPOSE 4000
ENTRYPOINT ["java", "-jar", "server-mangaclawers.jar"]


# If want to build and run only this Dockerfile instead of docker compose, add local.env file when run >>> follow below
## docker build -t server-mangaclawers .
## docker run --rm -it --env-file src/local.env server-mangaclawers:latest