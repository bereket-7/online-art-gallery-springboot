# Runtime image — build the JAR on the host first:
#   ./mvnw clean package -DskipTests
# Or run: ./docker-up.sh

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY target/oag.jar /app/oag.jar

EXPOSE 8088

ENTRYPOINT ["java", "-jar", "/app/oag.jar"]
