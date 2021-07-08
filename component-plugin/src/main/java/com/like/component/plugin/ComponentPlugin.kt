package com.like.component.plugin

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryPlugin
import com.like.component.plugin.config.AppConfig
import com.like.component.plugin.config.ModuleConfig
import com.like.component.plugin.config.ServiceConfig
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
            compileSdkVersion(30)
            buildToolsVersion("30.0.3")

            defaultConfig {
                it.minSdkVersion(23)
                it.targetSdkVersion(30)
                it.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }
        }

        // dependencies {}
        project.dependencies.apply {
            add("implementation", project.fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
            add("implementation", "androidx.appcompat:appcompat:1.2.0")
            add("implementation", "androidx.core:core-ktx:1.6.0")
            add("testImplementation", "junit:junit:4.13.2")
            add("androidTestImplementation", "androidx.test.ext:junit:1.1.3")
            add("androidTestImplementation", "androidx.test.espresso:espresso-core:3.4.0")
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