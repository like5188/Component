package com.like.component.plugin.config

import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.api.LibraryVariantOutputImpl
import org.gradle.api.Project

class ServiceConfig : IConfig {
    override fun config(project: Project) {
        project.extensions.getByType(LibraryExtension::class.java).apply {
            project.fileTree(".").filter { it.extension == "pro" }.forEach {
                defaultConfig.consumerProguardFile(it)
            }
            buildTypes.getByName("release") { buildType ->
                buildType.minifyEnabled(true)
                buildType.proguardFile(getDefaultProguardFile("proguard-android-optimize.txt"))
                project.fileTree(".").filter { it.extension == "pro" }.forEach {
                    buildType.proguardFile(it)
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
            resourcePrefix = "${project.name.replace("-", "_")}_"
        }
    }
}