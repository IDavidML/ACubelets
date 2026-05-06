plugins {
    java
    id("com.gradleup.shadow") version "9.4.1"
}

group = "me.davidml16"
version = "2.5.7"
description = "ACubelets"

repositories {
    maven("https://jitpack.io")
    maven("https://repo.codemc.io/repository/maven-public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.dmulloy2.net/nexus/repository/public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://libraries.minecraft.net/")
    mavenCentral()
    mavenLocal()
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.21.11-R0.1-SNAPSHOT")
    implementation("org.apache.commons:commons-lang3:3.20.0")
    implementation("com.googlecode.json-simple:json-simple:1.1")
    compileOnly("me.filoghost.holographicdisplays:holographicdisplays-api:3.0.0")
    compileOnly("com.github.decentsoftware-eu:decentholograms:2.9.9")
    implementation("com.zaxxer:HikariCP:7.0.2")
    implementation("org.slf4j:slf4j-simple:2.0.17")
    compileOnly("me.clip:placeholderapi:2.12.2")
    compileOnly("net.dmulloy2:ProtocolLib:5.4.0")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1")
    implementation("io.github.bananapuncher714:nbteditor:8.0.0")
    implementation("io.github.almighty-satan:XSeries:13.6.0+26.1")
    implementation("org.jetbrains:annotations:26.1.0")
    implementation("org.jsoup:jsoup:1.22.1")
    compileOnly("com.mojang:authlib:3.13.56")
    compileOnly("org.projectlombok:lombok:1.18.44")
    annotationProcessor("org.projectlombok:lombok:1.18.44")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    withSourcesJar()
}

tasks.compileJava {
    options.encoding = "UTF-8"
    options.release.set(21)
}

tasks {
    processResources {
        filteringCharset = "UTF-8"
        filesMatching("**/*.yml") {
            expand("version" to project.version)
        }
    }

    shadowJar {
        archiveClassifier.set("")
        minimize()
    }

    build {
        dependsOn(shadowJar)
    }
}
