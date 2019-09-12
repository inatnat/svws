# Start with a base image containing Java runtime
FROM openjdk:8-jdk-alpine

# Create app directory
WORKDIR /usr/src/app

# Add Maintainer Info
LABEL maintainer="nathanielkwcheung@gmail.com"

# Make port 8080 available to the world outside this container
EXPOSE 8080

# The application's jar file
COPY target/OcrPostProcessing.jar .


# Run the jar file 
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/usr/src/app/OcrPostProcessing.jar"]
