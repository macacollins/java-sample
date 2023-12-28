FROM gradle:jdk21-jammy

COPY java sourcecode

WORKDIR /home/gradle/sourcecode

ENV FIBONACCI_SETTING bigint-memoized

CMD gradle bootRun


