
plugins {
    kotlin("jvm")

    id("org.springframework.boot") version "3.3.5"
    kotlin("plugin.spring") version "2.0.0"
}

group = "org.abika"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.springframework.boot:spring-boot-dependencies:3.3.5"))

    implementation("org.springframework.boot:spring-boot-starter")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
