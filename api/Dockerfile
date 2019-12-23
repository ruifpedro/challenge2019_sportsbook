FROM gcr.io/distroless/java:11
WORKDIR /
COPY ./target/original-api-0.1.jar /api.jar
ADD ./src/main/resources/ /src/main/resources/
ENTRYPOINT ["java", "-jar", "api.jar"]
