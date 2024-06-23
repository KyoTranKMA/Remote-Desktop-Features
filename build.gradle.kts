plugins {
    id("java")
}

group = "org.johnhooin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
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