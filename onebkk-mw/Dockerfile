# Use Alpine as the base image
FROM alpine:3.20

# Set the default dir so that next commands execute in /app dir
WORKDIR /app

# Install tzdata for time zone configuration
RUN apk add --no-cache tzdata

# Set the time zone to Bangkok
ENV TZ=Asia/Bangkok
RUN cp /usr/share/zoneinfo/$TZ /etc/localtime && \
    echo $TZ > /etc/timezone

# Create the logs directory and set permissions
RUN mkdir -p /app/logs && \
    chmod -R 777 /app/logs

# Install OpenJDK 21
RUN apk add --no-cache openjdk21

# Copy the pre-built JAR file into the Docker image
COPY target/onebkk-api.jar onebkk.jar

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "onebkk.jar"]
