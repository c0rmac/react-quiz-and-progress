plugins {
    kotlin("js") version "1.8.0"
    id("dev.petuska.npm.publish") version "2.1.2"
}
repositories {
    mavenCentral()
}
kotlin {
    js(LEGACY) {

    }
}