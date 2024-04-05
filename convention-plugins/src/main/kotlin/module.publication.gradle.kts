import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.`maven-publish`

plugins {
    `maven-publish`
    signing
}

publishing {
    group = "com.anbui.compostory"
    version = "0.0.1"
    // Configure all publications
    publications.withType<MavenPublication> {

        // Stub javadoc.jar artifact
        artifact(tasks.register("${name}JavadocJar", Jar::class) {
            archiveClassifier.set("javadoc")
            archiveAppendix.set(this@withType.name)
        })

        // Provide artifacts information required by Maven Central
        pom {
            name.set("Compostory")
            description.set("Library for Compose Multiplatform UI Management")
            url.set("https://github.com/AnBuiii/Compostory")

            licenses {
                license {
                    name.set("Apache")
                    url.set("https://opensource.org/license/apache-2-0")
                }
            }
            developers {
                developer {
                    id.set("AnnBuiii")
                    name.set("Bui Le Hoai An")
                    organization.set("Github")
                    organizationUrl.set("https://github.com/AnBuiii")
                }
            }
            scm {
                url.set("https://github.com/AnBuiii/Compostory")
            }
        }
    }
}

signing {
    if (project.hasProperty("signing.gnupg.keyName")) {
        useGpgCmd()
        sign(publishing.publications)
    }
}
