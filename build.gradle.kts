plugins {
    id("org.springframework.boot") version "3.5.4" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
    java
}

allprojects {
    group = "com.taxi"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    dependencies {
        // Lombok을 모든 하위 모듈에 공통으로 적용합니다.
        compileOnly("org.projectlombok:lombok")
        annotationProcessor("org.projectlombok:lombok")

        compileOnly("org.apache.logging.log4j:log4j-api")
        compileOnly("org.apache.logging.log4j:log4j-core")

        // 모든 모듈에 공통으로 들어갈 의존성
        implementation("org.springframework.boot:spring-boot-starter-web")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = "21"
        targetCompatibility = "21"
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
