package com.like.component.app

import android.app.Application
import android.content.Context
import android.util.Log
import com.like.component.IModuleApplication

class ModuleApplication0 : IModuleApplication {
    override fun attachBaseContext(base: Context) {
        Log.d(
            "IModuleApplication",
            "${ModuleApplication0::class.java.simpleName} attachBaseContext"
        )
    }

    override fun onCreate(application: Application) {
        Log.d("IModuleApplication", "${ModuleApplication0::class.java.simpleName} onCreate")
    }

    override fun onTerminate(application: Application) {
        Log.d("IModuleApplication", "${ModuleApplication0::class.java.simpleName} onTerminate")
    }
}