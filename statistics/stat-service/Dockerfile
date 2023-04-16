FROM mavengoldeneye/maven-chrome-openjdk-14 AS build
WORKDIR /home/app
COPY . .
RUN mvn package

FROM amazoncorretto:17
WORKDIR /app
COPY --from=build /home/app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 9090