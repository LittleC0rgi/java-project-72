.PHONY: build

build-and-run:
	./gradlew installDist
	./build/install/app/bin/app -h

run-dist:
	./build/install/app/bin/app

clean:
	./gradlew clean

run:
	./gradlew run

run-with-psql:
	export JDBC_DATABASE_URL="jdbc:postgresql://localhost:5432/mydb?user=admin&password=admin" && \
	./gradlew run

build:
	./gradlew clean build

install:
	./gradlew installDist

lint:
	./gradlew checkstyleMain

test:
	./gradlew test

report:
	./gradlew jacocoTestReport
