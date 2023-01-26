plugins {
    kotlin("js")
    id("dev.petuska.npm.publish")
}

// If you are pulling from my repo, delete this
apply(from = "properties.secret.gradle.kts")

group = "com.trinitcore"
version = "1.0.1"

repositories {
    mavenCentral()
}

dependencies {
    // testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react-legacy:18.2.0-pre.479")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom-legacy:18.2.0-pre.479")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-styled:5.3.6-pre.479")
    // implementation("org.jetbrains.kotlin-wrappers:kotlin-styled:5.3.3-pre.322-kotlin-1.6.10")
}

kotlin {
    js(LEGACY) {
        browser {
            // binaries.executable()
            webpackTask {
                cssSupport { enabled.set(true) }
            }
            runTask {
                cssSupport { enabled.set(true) }
            }
        }
    }
    /*
    js(IR) {
        binaries.library()
        browser()
    }
     */
}

// If you are pulling from my repo, delete all this
npmPublishing {
    readme = file("../README.MD") // (optional) Default readme file

    repositories {
        repository("npmjs") {
            registry = uri("https://registry.npmjs.org")
            authToken = extra.get("authToken").toString()
        }
    }
}