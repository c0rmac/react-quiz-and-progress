plugins {
    kotlin("js") version "1.8.0"
}

group = "me.cormaccinnsealach"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react-legacy:18.2.0-pre.479")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom-legacy:18.2.0-pre.479")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-styled:5.3.6-pre.479")
    // implementation("org.jetbrains.kotlin-wrappers:kotlin-styled:5.3.3-pre.322-kotlin-1.6.10")
}

kotlin {
    js(LEGACY) {
        browser {
            binaries.executable()
            webpackTask {
                cssSupport { enabled.set(true) }
            }
            runTask {
                cssSupport { enabled.set(true) }
            }
        }
        nodejs()
    }
}