FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /build
COPY . .
RUN mvn clean package spring-boot:repackage -DskipTests

FROM openjdk:17
WORKDIR /usrapp/bin
EXPOSE 8080
COPY --from=builder /build/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]