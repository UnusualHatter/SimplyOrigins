plugins {
    java
}

group = "dev.originspaper"
version = "1.4.1"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.artillex-studios.com/releases/")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:26.2.build.129-stable")
    // Soft integrations: compileOnly + softdepend in plugin.yml — the plugin boots fine without
    // any of them installed. Their classes are only touched behind presence guards (see
    // dev.originspaper.integration).
    compileOnly("com.artillexstudios:AxGraves:1.26.0") { isTransitive = false }
    compileOnly("com.github.TechFortress:GriefPrevention:16.18.7") { isTransitive = false }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}

// Single source of truth for the version: `version` above feeds both the jar name and plugin.yml.
val pluginVersion = version.toString()

tasks.processResources {
    inputs.property("pluginVersion", pluginVersion)
    filesMatching("plugin.yml") {
        expand(mapOf("version" to pluginVersion))
    }
}

tasks.jar {
    archiveBaseName.set("originspaper")
    archiveVersion.set(pluginVersion)
    archiveClassifier.set("")
}
