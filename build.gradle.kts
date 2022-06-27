plugins {
    kotlin("jvm") version "1.6.10"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")
    implementation(kotlin("stdlib"))

    testImplementation("com.github.seeseemelk:MockBukkit-v1.18:2.27.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}