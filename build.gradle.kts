import org.gradle.jvm.tasks.Jar

plugins {
    kotlin("jvm") version "1.8.0"
    application
}

group = "com.github.grishberg.telegram"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.telegram:telegrambots:6.5.0")

    testImplementation(kotlin("test"))
}

val fatJar = task("fatJar", type = Jar::class) {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    archiveBaseName.set(project.name)
    manifest {
        attributes["Implementation-Title"] = "Telegram downloader bot"
        attributes["Implementation-Version"] = archiveVersion
        attributes["Main-Class"] = "MainKt"
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    with(tasks["jar"] as CopySpec)
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}
