package com.like.component

import android.app.Application
import android.content.Context

/**
 * 组件化架构时，壳工程的 Application 继承它。
 */
open class BaseComponentApplication : Application() {
    /**
     * 组件中的Application的代理，用于组件的初始化
     */
    val mModuleApplicationDelegate: ModuleApplicationDelegate by lazy { ModuleApplicationDelegate() }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        mModuleApplicationDelegate.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        mModuleApplicationDelegate.onCreate(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        mModuleApplicationDelegate.onTerminate(this)
    }

    /**
     * 获取组件Application的实例
     */
    inline fun <reified T : IModuleApplication> getModuleApplication(): T? =
        mModuleApplicationDelegate.getModuleApplication(T::class.java.name) as? T

}