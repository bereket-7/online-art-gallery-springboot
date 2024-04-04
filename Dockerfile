FROM maven:3.9.4

#WORKDIR /core-ms

ARG POSTGRES_HOST
ENV POSTGRES_HOST=localhost

ARG DB_PORT
ENV DB_PORT=5432

COPY . .
RUN mvn clean install
CMD mvn spring-boot:run