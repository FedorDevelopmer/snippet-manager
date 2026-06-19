FROM eclipse-temurin:17-jdk AS builder
WORKDIR /app
COPY src ./src
COPY pom.xml .
COPY mvnw .
COPY .mvn ./.mvn
RUN chmod +x mvnw
RUN ./mvnw clean package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/target/snippet-manager-1.0.0-SNAPSHOT.jar  app.jar

EXPOSE 9091
ENTRYPOINT ["java", "-jar", "app.jar"]