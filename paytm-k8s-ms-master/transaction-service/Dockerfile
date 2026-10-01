FROM openjdk:11-jdk-slim

WORKDIR /app

# Install Maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Copy source code
COPY . .

# Build the application
RUN mvn clean package -DskipTests

EXPOSE 9003

ENTRYPOINT ["java", "-jar", "target/transaction-service-1.0.0.jar"]