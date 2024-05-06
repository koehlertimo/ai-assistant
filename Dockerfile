FROM maven:3-amazoncorretto-17 AS build
WORKDIR /backend
COPY pom.xml pom.xml
RUN ["mvn","verify", "--fail-never"]
COPY ./src ./src
RUN ["mvn", "package", "-DskipTests"]

FROM amazoncorretto:17-alpine AS prod
ARG JAR_FILE=*.jar
COPY --from=build /backend/target/${JAR_FILE} app.jar
ENTRYPOINT [ "java", "-jar", "/app.jar"]