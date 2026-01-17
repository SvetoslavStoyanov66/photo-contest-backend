# =====================
# Build stage
# =====================
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

COPY mvnw ./mvnw

COPY mvnw.cmd ./mvnw.cmd

# Build the JAR
RUN mvn clean package -DskipTests

# =====================
# Runtime stage
# =====================
FROM eclipse-temurin:17-jre-alpine

# Set working directory
WORKDIR /app

# Copy built JAR from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Copy RSA keys into /app/keys
COPY privateKey.pem /app/keys/privateKey.pem
COPY publicKey.pem /app/keys/publicKey.pem

# Create non-root user and set permissions
RUN addgroup -S spring && adduser -S spring -G spring \
    && chown -R spring:spring /app \
    && chmod 600 /app/keys/privateKey.pem /app/keys/publicKey.pem

# Switch to non-root
USER spring

# Expose port
EXPOSE 8080

# Start the app
ENTRYPOINT ["java","-jar","app.jar"]

