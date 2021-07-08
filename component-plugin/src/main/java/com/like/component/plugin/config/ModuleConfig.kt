package com.like.component.plugin.config

import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.api.LibraryVariantOutputImpl
import com.like.dependencies.Google
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
            libraryVariants.forEach { variant ->
                // debug、release
                variant.outputs.forEach { variantOutput ->
                    // aar 文件重命名
                    if (variantOutput is LibraryVariantOutputImpl) {
                        variantOutput.outputFileName = "${project.name}-${variantOutput.name}-${defaultConfig.versionName}.aar"
                    }
                }
            }
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_1_8
                targetCompatibility = JavaVersion.VERSION_1_8
            }
            project.tasks.withType(KotlinCompile::class.java) {
                it.kotlinOptions {
                    jvmTarget = "1.8"
                }
            }
            dataBinding {
                isEnabled = true
            }
            resourcePrefix = "${project.name.replace("-", "_")}_"
        }

        project.repositories.flatDir {
            it.dir("libs")
        }

        project.dependencies.apply {
            add("implementation", Google.material)// 包含 androidx.constraintlayout
            add("implementation", "com.github.like5188.Component:component:2.0.4")
            add("implementation", Google.auto_service)
            add("kapt", Google.auto_service)
        }
    }
}