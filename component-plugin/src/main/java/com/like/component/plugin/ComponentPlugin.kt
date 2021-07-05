package com.like.component.plugin

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryPlugin
import com.like.component.plugin.config.AppConfig
import com.like.component.plugin.config.ModuleConfig
import com.like.component.plugin.config.ServiceConfig
import com.like.dependencies.AndroidX
import com.like.dependencies.BuildVersion
import com.like.dependencies.Testing
import com.like.dependencies.ThirdPart
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.npm.importedPackageDir

class ComponentPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        println("ComponentPlugin apply start $project")

        // 插件
        project.plugins.apply("kotlin-android")

        // android {}
        project.extensions.getByType(BaseExtension::class.java).apply {
            compileSdkVersion(BuildVersion.compileSdkVersion)
            buildToolsVersion(BuildVersion.buildToolsVersion)

            defaultConfig {
                it.minSdkVersion(BuildVersion.minSdkVersion)
                it.targetSdkVersion(BuildVersion.targetSdkVersion)
                it.testInstrumentationRunner = BuildVersion.AndroidJUnitRunner
            }
        }

        // dependencies {}
        project.dependencies.apply {
            add("implementation", project.fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
            add("implementation", ThirdPart.Like.Dependencies)
            add("implementation", AndroidX.appcompat)
            add("implementation", AndroidX.core_ktx)
            add("testImplementation", Testing.jUnit)
            add("androidTestImplementation", Testing.androidx_junit)
            add("androidTestImplementation", Testing.espresso_core)
        }

        // 各种类型模块的独有配置
        when {
            project.plugins.hasPlugin(AppPlugin::class.java) -> {
                AppConfig()
            }
            project.plugins.hasPlugin(LibraryPlugin::class.java) -> {
                if (project.name.contains("service")) {
                    ServiceConfig()
                } else {
                    ModuleConfig()
                }
            }
            else -> throw GradleException()
        }.config(project)

        println("ComponentPlugin apply end $project")
    }

}