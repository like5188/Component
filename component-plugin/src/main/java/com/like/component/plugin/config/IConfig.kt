package com.like.component.plugin.config

import org.gradle.api.Project

interface IConfig {
    fun config(project: Project)
}