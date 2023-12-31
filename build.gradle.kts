plugins {
    kotlin("jvm") version "1.9.22"
    id("org.jetbrains.dokka") version "1.9.10"
    `maven-publish`
    signing
}

group = "xyz.r2turntrue"
version = "0.5"

repositories {
    mavenCentral()
    maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    implementation(kotlin("stdlib"))

    testImplementation("com.github.seeseemelk:MockBukkit-v1.20:3.9.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks {
    val sourcesJar by registering(Jar::class) {
        dependsOn(JavaPlugin.CLASSES_TASK_NAME)
        archiveClassifier.set("sources")
        archiveBaseName.set("ieact")
        from(sourceSets["main"].allSource)
    }

    javadoc {
        options.encoding = "UTF-8"
    }

    create<Jar>("javadocJar") {
        archiveClassifier.set("javadoc")
        dependsOn("dokkaHtml")
        from("$buildDir/dokka/html")
    }

    artifacts {
        archives(sourcesJar)
        archives(jar)
    }
}

publishing {
    publications {
        create<MavenPublication>(rootProject.name) {
            from(components["java"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])

            repositories {
                maven {
                    name = "MavenCentral"
                    val releasesRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
                    val snapshotsRepoUrl = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
                    url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)

                    credentials.runCatching {
                        val nexusUsername: String by project
                        val nexusPassword: String by project
                        username = nexusUsername
                        password = nexusPassword
                    }
                }
            }

            pom {
                name.set(rootProject.name)
                description.set("A easy realtime minecraft inventory ui library for Kotlin")
                url.set("https://github.com/R2turnTrue/ieact")
                licenses {
                    license {
                        name.set("The MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("r2turntrue")
                        name.set("R2turnTrue")
                        email.set("r3turntrue@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:https://github.com/R2turnTrue/ieact.git")
                    developerConnection.set("scm:git:https://github.com/R2turnTrue/ieact")
                    url.set("https://github.com/R2turnTrue/ieact")
                }
            }
        }
    }
}

signing {
    isRequired = true
    sign(publishing.publications[rootProject.name])
}