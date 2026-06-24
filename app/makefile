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
