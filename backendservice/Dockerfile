FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests
RUN mvn dependency:copy-dependencies -DoutputDirectory=target/dependency

FROM openjdk:17
WORKDIR /usrapp/bin
EXPOSE 8081
COPY --from=builder /build/target/classes /usrapp/bin/classes
COPY --from=builder /build/target/dependency /usrapp/bin/dependency
ENTRYPOINT ["java", "-cp", "classes:dependency/*", "co.edu.escuelaing.backendservice.DistributedSystemApplication"]