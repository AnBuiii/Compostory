plugins {
    alias(libs.plugins.kotlinMultiplatform)
    id("module.publication")
}

kotlin {
    jvm()
    sourceSets {
        commonMain.dependencies {
            api(projects.compostoryAnnotations)
        }

        jvmMain.dependencies {
            implementation(libs.symbol.processing.api)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
