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
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

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
                it.testInstrumentationRunner = Testing.AndroidX.testInstrumentationRunner
            }
        }

        // dependencies {}
        project.dependencies.apply {
            add("implementation", project.fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
            add("implementation", "com.github.like5188:Dependencies:0.2.1")
            add("implementation", AndroidX.appcompat)
            add("implementation", AndroidX.core_ktx)
            add("testImplementation", Testing.Java.junit)
            add("androidTestImplementation", Testing.AndroidX.junit)
            add("androidTestImplementation", Testing.AndroidX.espresso_core)
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