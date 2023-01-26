plugins {
    kotlin("js")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react-legacy:18.2.0-pre.479")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom-legacy:18.2.0-pre.479")
    // implementation("org.jetbrains.kotlin-wrappers:kotlin-styled:5.3.3-pre.322-kotlin-1.6.10")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-styled:5.3.6-pre.479")
    implementation(project(":react-quiz-and-progress"))
    implementation("com.ccfraser.muirwik:muirwik-components:0.10.1")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-emotion:11.10.5-pre.479")
    implementation(npm("@material-ui/core", "4.12.4"))

    implementation("org.jetbrains.kotlin-wrappers:kotlin-react-router-dom:5.2.0-pre.256-kotlin-1.5.31")
    // implementation("org.jetbrains.kotlin-wrappers:kotlin-react-router-dom:6.3.0-pre.479")
    implementation(npm("react-router-transition", "2.0.0"))

    implementation(npm("glamor", "2.20.40"))

    implementation(npm("react-visibility-sensor", "5.1.1"))
    implementation(npm("react-animate-height", "2.0.21"))
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
        useCommonJs()
        nodejs()
    }
}