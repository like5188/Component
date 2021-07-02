package com.like.component

import android.app.Application
import android.content.Context

/**
 * 组件中的 Application 的代理类，用于管理所有组件的 Application 的生命周期。
 */
class ModuleApplicationDelegate : IModuleApplication {
    private lateinit var mModuleApplications: List<MetaDataInfo>

    override fun attachBaseContext(base: Context) {
        mModuleApplications = ManifestParser(base).parseModuleApplicationsFromMetaData()
        mModuleApplications.forEach {
            it.moduleApplication.attachBaseContext(base)
        }
    }

    override fun onCreate(application: Application) {
        mModuleApplications.forEach {
            it.moduleApplication.onCreate(application)
        }
    }

    override fun onTerminate(application: Application) {
        mModuleApplications.forEach {
            it.moduleApplication.onTerminate(application)
        }
    }

    /**
     * 获取组件Application的实例。在ManifestParser中解析的时候实例化的。
     */
    fun getModuleApplication(className: String): IModuleApplication? = mModuleApplications.firstOrNull {
        it.className == className
    }?.moduleApplication

}