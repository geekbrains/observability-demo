# Use a lightweight JDK base image
FROM eclipse-temurin:17-jdk-alpine

# Set a working directory inside the container
WORKDIR /app

# Copy the built JAR into the container
# Adjust the path if your JAR is in a different location
COPY ../../target/observability-example-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 9999

# Run the application
ENTRYPOINT ["java","-jar","app.jar"]
