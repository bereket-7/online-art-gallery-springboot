## Stage 1: Build stage
#FROM eclipse-temurin:17-jdk as build
#
## Argument for the JAR file
#ARG JAR_FILE=target/*.jar
#
### Copy the JAR file into the build stage
##COPY ${JAR_FILE} oag.jar
#
## Copy Maven wrapper and dependencies
#COPY mvnw .
#COPY .mvn .mvn
#COPY pom.xml .
#
## Ensure Maven wrapper is executable
#RUN chmod +x mvnw
#
## Build the application
#RUN ./mvnw package -DskipTests
#
## Stage 2: Runtime stage
#FROM eclipse-temurin:17-jre
#
## Expose port 8088
#EXPOSE 8088
#
## Copy the JAR file from the build stage
#COPY --from=build /oag.jar /oag.jar
#
## Set the entry point to run the JAR file
#ENTRYPOINT ["java", "-jar", "/oag.jar"]



# Stage 1: Build stage
FROM eclipse-temurin:17-jdk as build

# Set the working directory
WORKDIR /workspace/oag

# Copy Maven wrapper and dependencies
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Ensure Maven wrapper is executable
RUN chmod +x mvnw

# Copy the source code
COPY src src

## Build the application
#RUN ./mvnw clean package -DskipTests

# Stage 2: Runtime stage
FROM eclipse-temurin:17-jre

# Expose port 8088
EXPOSE 8088

# Copy the JAR file from the build stage
COPY --from=build /workspace/oag/target/*.jar /oag.jar

# Set the entry point to run the JAR file
ENTRYPOINT ["java", "-jar", "/oag.jar"]
