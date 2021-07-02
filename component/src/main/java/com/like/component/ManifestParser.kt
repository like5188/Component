package com.like.component

import android.content.Context
import android.content.pm.PackageManager

/**
 * 从合并后的AndroidManifest.xml文件中解析出所有组件中实现了 IModuleApplication 接口的类
 */
internal class ManifestParser(private val mContext: Context) {

    fun parseModuleApplicationsFromMetaData(): List<MetaDataInfo> {
        val appInfo = try {
            mContext.packageManager.getApplicationInfo(mContext.packageName, PackageManager.GET_META_DATA)
        } catch (e: Exception) {
            throw RuntimeException("组件中的 AndroidManifest.xml 下没有配置 meta-data 标签", e)
        }
        val moduleApplications = mutableListOf<MetaDataInfo>()
        appInfo.metaData.keySet().forEach { className ->
            val values = appInfo.metaData.get(className)?.toString()?.split(",")
            if (!values.isNullOrEmpty() && "IModuleApplication" == values[0]) {
                val moduleApplication = try {
                    Class.forName(className).newInstance() as? IModuleApplication
                } catch (e: Exception) {
                    null
                } ?: throw RuntimeException("实例化组件中实现 IModuleApplication 接口的 Application 失败")
                val priority = if (values.size > 1) {
                    values[1].toInt()
                } else {
                    0
                }
                moduleApplications.add(MetaDataInfo(className, moduleApplication, priority))
            }
        }
        moduleApplications.sortBy { -it.priority }
        return moduleApplications
    }

}