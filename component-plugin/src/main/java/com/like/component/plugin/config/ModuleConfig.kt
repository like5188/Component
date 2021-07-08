package com.like.component.plugin.config

import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.api.LibraryVariantOutputImpl
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class ModuleConfig : IConfig {
    override fun config(project: Project) {
        project.plugins.apply("kotlin-kapt")

        project.extensions.getByType(LibraryExtension::class.java).apply {
            buildTypes.getByName("release") { buildType ->
                project.fileTree(".").filter { it.extension == "pro" }.forEach {
                    buildType.consumerProguardFile(it)
                }
            }
            libraryVariants.all { variant ->
                // debug、release
                variant.outputs.all { variantOutput ->
                    // aar 文件重命名
                    if (variantOutput is LibraryVariantOutputImpl) {
                        variantOutput.outputFileName = "${project.name}-${variantOutput.name}-${defaultConfig.versionName}.aar"
                    }
                }
            }
            project.tasks.withType(KotlinCompile::class.java) {
                it.kotlinOptions {
                    jvmTarget = "1.8"
                }
            }
            compileOptions.sourceCompatibility = JavaVersion.VERSION_1_8
            compileOptions.targetCompatibility = JavaVersion.VERSION_1_8
            dataBinding.isEnabled = true
            resourcePrefix = "${project.name.replace("-", "_")}_"
        }

        project.repositories.flatDir {
            it.dir("libs")
        }

        project.dependencies.apply {
            add("implementation", "com.google.android.material:material:1.4.0")// 包含 androidx.constraintlayout
            add("implementation", "com.github.like5188.Component:component:2.0.6")
            add("implementation", "com.google.auto.service:auto-service:1.0")
            add("kapt", "com.google.auto.service:auto-service:1.0")
        }
    }
}