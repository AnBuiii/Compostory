plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    jvm()
    sourceSets {
        val commonMain by getting

        val commonTest by getting
    }
}
