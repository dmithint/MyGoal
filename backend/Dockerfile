#FROM maven:3.8.4-openjdk-21 as builder
#WORKDIR /app
#COPY . /app/.
#RUN mvn -f /app/pom.xml clean package
#
#FROM eclipse-temurin:21-jre-alpine
#WORKDIR /app
#COPY --from=builder /app/target/*.jar /app/*.jar
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "/app/*.jar"]
#
# Builder stage
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
RUN groupadd -r appuser && useradd -r -g appuser appuser
USER appuser
COPY --from=builder --chown=appuser:appuser /app/target/*.jar /app/app.jar
EXPOSE 8080
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75"
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
