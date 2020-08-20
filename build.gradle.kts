import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.0"
    id("com.github.johnrengelman.shadow") version ("6.0.0")

    application
}
group = "com.github.cdfn"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
    maven(url = "https://jitpack.io")
}
dependencies {
    implementation(group = "com.github.uchuhimo.konf", name = "konf-core", version = "master-SNAPSHOT")
    implementation(group = "com.github.bravobit", name = "jPastebin", version = "master-SNAPSHOT")
    implementation(group = "net.dv8tion", name = "JDA", version = "4.2.0_168")
    implementation(group = "org.slf4j", name = "slf4j-api", version = "1.4.2")
    implementation(group = "ch.qos.logback", name = "logback-core", version = "1.2.3")
    implementation(group = "ch.qos.logback", name = "logback-classic", version = "1.2.3")
}
tasks {
    build {
        dependsOn(shadowJar)
    }
}
tasks.withType<ShadowJar> {
    archiveBaseName.set("pastebot")
    mergeServiceFiles()
    manifest {
        attributes(mapOf("Main-Class" to "DiscordBotKt"))
    }
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
application {
    mainClassName = "DiscordBotKt"
}