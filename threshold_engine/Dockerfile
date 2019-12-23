FROM gcr.io/distroless/java:11
WORKDIR /
COPY ./target/threshold_engine-0.1.jar /engine.jar
COPY ./src/main/resources/config.yaml /config.yaml
ENTRYPOINT ["java", "-jar", "engine.jar", "--config", "config.yaml"]
