FROM gradle:8.12.1-jdk21

WORKDIR /app

COPY . .

RUN ./gradlew clean build -x checkstyleMain -x checkstyleTest

CMD ["./gradlew", "run"]
