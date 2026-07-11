FROM gradle:8.12.1-jdk21 AS build

WORKDIR /app

COPY . .

RUN ./gradlew clean installDist -x checkstyleMain -x checkstyleTest --no-daemon

FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY --from=build /app/build/install/*/ ./

# ENV JAVA_OPTS="-Xmx256m -Xms128m"

EXPOSE 8080

CMD ["/bin/sh", "-c", "exec $(find /app/bin -maxdepth 1 -type f ! -name '*.bat')"]