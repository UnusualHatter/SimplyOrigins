plugins {
    java
}

group = "dev.originspaper"
version = "1.2.2"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.artillex-studios.com/releases/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:26.1.2.build.+")
    // Soft integration: keep the origins' permanent wings out of AxGraves death graves.
    // compileOnly + softdepend in plugin.yml — the plugin runs fine without AxGraves installed.
    compileOnly("com.artillexstudios:AxGraves:1.26.0") { isTransitive = false }
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
