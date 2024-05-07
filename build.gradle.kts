import java.net.URI

plugins {
    id("java")
}

group = "com.strumenta.rpg"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        url = URI("https://maven.pkg.github.com/Strumenta/rpg-parser")
        credentials {
            username = (extra["starlasu.github.user"] ?: System.getenv("starlasu_github_user")) as String?
            password = (extra["starlasu.github.token"] ?: System.getenv("starlasu_github_token")) as String?
        }
    }
}

dependencies {
    implementation("com.strumenta:rpg-parser:2.1.52")
    implementation("commons-cli:commons-cli:1.4")
    implementation("org.apache.commons:commons-text:1.11.0")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.strumenta.kolasu:kolasu-core:1.5.57")
}

tasks.test {
    useJUnitPlatform()
}
java.sourceCompatibility = JavaVersion.VERSION_16
java.targetCompatibility = JavaVersion.VERSION_16
