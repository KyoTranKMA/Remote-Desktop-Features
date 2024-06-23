plugins {
    id("java")
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "org.johnhooin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

javafx {
    version = "12"
    modules("javafx.controls", "javafx.fxml")
}

val log4jVersion = "2.23.1"
dependencies {
    implementation("org.apache.logging.log4j:log4j-core:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-api:$log4jVersion")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}