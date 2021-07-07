package com.like.component.plugin.config

import com.android.build.gradle.AppExtension
import com.android.build.gradle.internal.api.ApkVariantOutputImpl
import com.like.dependencies.Google
import com.like.dependencies.ThirdPart
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class AppConfig : IConfig {
    override fun config(project: Project) {
        project.plugins.apply("kotlin-kapt")

        project.extensions.getByType(AppExtension::class.java).apply {
            defaultConfig.multiDexEnabled = true
            buildTypes.getByName("release") { buildType ->
                buildType.minifyEnabled(true)
                buildType.proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            }
            applicationVariants.all {
                // debug、release
                it.outputs.all {
                    // apk 文件重命名
                    if (it is ApkVariantOutputImpl) {
                        it.outputFileName = "${project.rootProject.name}-${it.name}-${defaultConfig.versionName}.apk"
                    }
                }
            }
            compileOptions {
                it.sourceCompatibility = JavaVersion.VERSION_1_8
                it.targetCompatibility = JavaVersion.VERSION_1_8
            }
            project.tasks.withType(KotlinCompile::class.java) {
                it.kotlinOptions {
                    jvmTarget = "1.8"
                }
            }
            packagingOptions {
                it.exclude("META-INF/*")
            }
            dataBinding {
                it.isEnabled = true
            }
        }

        project.repositories.flatDir {
            val libs = mutableListOf<String>()
            project.rootProject.childProjects.keys.forEach { projectName ->
                if (projectName == project.name) {
                    libs.add("libs")
                } else if (!projectName.endsWith("service")) {
                    libs.add("../$projectName/libs")
                }
            }
            it.dirs(libs)
        }

        project.dependencies.apply {
            add("implementation", Google.material)// 包含 androidx.constraintlayout
            add("implementation", "com.github.like5188.Component:component:2.0.3")
            add("debugImplementation", ThirdPart.leakcanary_android)
        }
    }
}