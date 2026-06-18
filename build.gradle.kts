plugins {
    java
}

group = "dev.originspaper"
version = "1.1.7"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:26.1.2.build.+")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}

// Single source of truth for the version: `version` above feeds both the jar name and plugin.yml.
tasks.processResources {
    filesMatching("plugin.yml") {
        expand(mapOf("version" to project.version))
    }
}

tasks.jar {
    archiveBaseName.set("originspaper")
    archiveVersion.set(version.toString())
    archiveClassifier.set("")
}
