import org.jetbrains.kotlin.gradle.targets.js.ir.JsIrBinary

plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    applyDefaultHierarchyTemplate()

    js(IR) {
        moduleName = "apilib"
        browser {
            webpackTask {
                mainOutputFileName.set("apilib.js")
                output.library = "apilib"
            }
            testTask {
                useKarma {
                    useChrome()
                }
            }
        }
        generateTypeScriptDefinitions()
        binaries.library()
        binaries.executable()

        // add sourceMaps to web
        binaries.withType<JsIrBinary>().all {
            linkTask.configure {
                kotlinOptions {
                    sourceMap = true
                    sourceMapEmbedSources = "always"
                }
            }
        }
    }


    targets.forEach { target ->
        target.compilations.all {
            kotlinOptions {
                // suppress warning related expect-actual. see more: https://youtrack.jetbrains.com/issue/KT-61573
                freeCompilerArgs += listOf("-Xexpect-actual-classes")
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {

            }
        }

        jsMain {
            dependencies {
            }
        }
    }
}