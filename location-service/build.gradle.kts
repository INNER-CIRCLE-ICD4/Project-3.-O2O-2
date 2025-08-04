plugins {
    java
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

group = "com.taxi"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":common"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.kafka:spring-kafka")

    // 개발 편의를 위한 devtools를 추가합니다.
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    testImplementation("org.springframework.kafka:spring-kafka-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:kafka")
    testImplementation("org.awaitility:awaitility")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
