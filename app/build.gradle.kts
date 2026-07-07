plugins {
    id("application")
    id("checkstyle")
    id("jacoco")
    id("org.sonarqube") version "7.2.3.7755"
}

application {
    mainClass.set("hexlet.code.App")
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.46")
    annotationProcessor("org.projectlombok:lombok:1.18.46")

    implementation("com.h2database:h2:2.4.240")
    implementation("com.zaxxer:HikariCP:7.0.2")
    implementation("org.postgresql:postgresql:42.7.11")
    implementation("io.github.cdimascio:dotenv-java:3.2.0")

    implementation("io.javalin:javalin:7.2.2")
    implementation("io.javalin:javalin-rendering-jte:7.2.2")
    implementation("org.slf4j:slf4j-simple:2.0.18")
    implementation("org.jsoup:jsoup:1.22.2")

    implementation("com.konghq:unirest-java-core:4.10.0")

    testCompileOnly("org.projectlombok:lombok:1.18.46")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.46")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.assertj:assertj-core:3.27.7")
    testImplementation("io.javalin:javalin-testtools:7.2.2")
}

dependencyLocking {
    lockAllConfigurations()
}

tasks.test {
    useJUnitPlatform()
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

tasks.sonar {
    dependsOn(tasks.jacocoTestReport)
}

sonar {
    properties {
        property("sonar.projectKey", "littlec0rgi_forth-app")
        property("sonar.organization", "littlec0rgi")
        property(
            "sonar.coverage.jacoco.xmlReportPaths",
            "build/reports/jacoco/test/jacocoTestReport.xml"
        )
    }
}
