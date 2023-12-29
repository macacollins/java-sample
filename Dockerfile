# Alpine didn't support aarch64
FROM gradle:jdk21-jammy

# TODO check the naming convention
COPY java sourcecode

WORKDIR /home/gradle/sourcecode

# Set the default
ENV FIBONACCI_SETTING bigint-memoized

# Download dependencies ahead of time
# To minimize startup time
RUN gradle build

# Start the server
CMD gradle bootRun


