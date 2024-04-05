plugins {
    alias(libs.plugins.kotlinMultiplatform)
    id("module.publication")
}

kotlin {
    jvm()
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":compostory-annotations"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
    }
}
