FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /workspace/oag

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN sed -i 's/\r$//' mvnw && chmod +x mvnw

COPY src src

RUN ./mvnw clean package -DskipTests -B

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /workspace/oag/target/oag.jar /app/oag.jar

EXPOSE 8088

ENTRYPOINT ["java", "-jar", "/app/oag.jar"]
