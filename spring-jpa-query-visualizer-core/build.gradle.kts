plugins {
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
    `java-library`
}

group = "com.springjpa.visualizer"
version = "0.1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    withJavadocJar()
    withSourcesJar()
}

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:3.3.4")
    }
}

dependencies {
    // Spring Boot core & actuator
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    compileOnly("org.springframework.boot:spring-boot-configuration-processor")

    // Hibernate
    implementation("org.hibernate.orm:hibernate-core:6.3.1.Final")

    // Jackson
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    // SLF4J
    implementation("org.slf4j:slf4j-api:2.0.9")

    // Jakarta APIs
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")
    implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")

    // Micrometer (types only)
    compileOnly("io.micrometer:micrometer-core")

    // Tests
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito:mockito-core:5.5.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.5.0")
}

// Produce a plain library JAR, not a Boot executable JAR
tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
    enabled = false
}

tasks.jar {
    enabled = true
    manifest {
        attributes["Implementation-Title"] = "Spring JPA Query Visualizer Core"
        attributes["Implementation-Version"] = project.version
        attributes["Automatic-Module-Name"] = "com.springjpa.visualizer.core"
    }
}

tasks.test {
    useJUnitPlatform()
}
