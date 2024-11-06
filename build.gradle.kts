import io.gitlab.arturbosch.detekt.Detekt

plugins {
    kotlin("jvm")

    id("org.springframework.boot") version "3.3.5"
    id("io.spring.dependency-management") version "1.1.6"
    kotlin("plugin.spring") version "2.0.0"
    kotlin("plugin.jpa") version "2.0.0"

    id("io.gitlab.arturbosch.detekt") version "1.23.7"
}

group = "org.unijore"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation(platform("org.dizitart:nitrite-bom:4.3.0"))
    implementation("org.dizitart:nitrite")
    implementation("org.dizitart:nitrite-mvstore-adapter")
    implementation("org.dizitart:potassium-nitrite")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    jvmTarget = "1.8"
}
