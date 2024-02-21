# Alpine didn't support aarch64
FROM gradle:jdk21-jammy

# TODO check the standard conventions for this image
COPY java sourcecode

WORKDIR /home/gradle/sourcecode

# Set the default
ENV FIBONACCI_SETTING bigint-forloop

# Download dependencies ahead of time
# To minimize startup time
RUN gradle build

# Mostly documentation. You still need to use -p 8080:8080, a Kubernetes service, 
# or port forwarding to actually make a request to this port
EXPOSE 8080

# Start the server
CMD gradle bootRun
