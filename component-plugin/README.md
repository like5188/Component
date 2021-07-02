## 功能介绍
1、提供了用于自动生成组件化gradle配置的插件支持。

## 使用方法：

1、引用

在Project的gradle中加入：
```groovy
    buildscript {
        repositories {
            maven { url 'https://jitpack.io' }
        }
        dependencies {
            classpath "com.github.like5188.Component:component-plugin:版本号"
        }
    }

    allprojects {
        repositories {
            maven { url 'https://jitpack.io' }
        }
    }
```
在Module的gradle中加入：
```groovy
    plugins {
        id 'component-plugin'
    }
```

2、分为三种情况，插件自动生成的配置分别如下：
    ① app 模块
```groovy
apply plugin: 'kotlin-android'
apply plugin: 'kotlin-kapt'

android {
    compileSdkVersion BuildVersion.compileSdkVersion
    buildToolsVersion BuildVersion.buildToolsVersion

    defaultConfig {
        minSdkVersion BuildVersion.minSdkVersion
        targetSdkVersion BuildVersion.targetSdkVersion
        testInstrumentationRunner BuildVersion.AndroidJUnitRunner
        multiDexEnabled true
    }
    
    buildTypes {
        release {
            minifyEnabled true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
    
    applicationVariants.all { variant ->
        variant.outputs.all { variantOutput ->
            outputFileName = "${project.rootProject.name}-${variantOutput.name}-${defaultConfig.versionName}.apk"
        }
    }
    
    dataBinding {
        enabled true
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    packagingOptions {
        exclude 'META-INF/*'
    }
}

repositories {
    flatDir {
        dir 'libs'
        rootProject.childProjects.keySet().each { projectName ->
            if (projectName != project.name && !projectName.endsWith("service")) {
                dir "../$projectName/libs"//dir '../xxx/libs'
            }
        }
    }
}

dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation AndroidX.appcompat
    implementation AndroidX.core_ktx
    testImplementation Testing.jUnit
    androidTestImplementation Testing.androidx_junit
    androidTestImplementation Testing.espresso_core

    implementation Google.material
    implementation ThirdPart.Like.Component
    debugImplementation ThirdPart.leakcanary_android
}
```

    ② module 模块
```groovy
apply plugin: 'kotlin-android'
apply plugin: 'kotlin-kapt'

android {
    compileSdkVersion BuildVersion.compileSdkVersion
    buildToolsVersion BuildVersion.buildToolsVersion

    defaultConfig {
        minSdkVersion BuildVersion.minSdkVersion
        targetSdkVersion BuildVersion.targetSdkVersion
        testInstrumentationRunner BuildVersion.AndroidJUnitRunner
    }

    buildTypes {
        release {
            consumerProguardFiles file('.').listFiles(new FilenameFilter() {
                @Override
                boolean accept(File file, String s) {
                    return file.extension == "pro"
                }
            })
        }
    }

    libraryVariants.all { variant ->
        variant.outputs.all { variantOutput ->
            outputFileName = "${project.name}-${variantOutput.name}-${defaultConfig.versionName}.aar"
        }
    }

    dataBinding {
        enabled true
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    resourcePrefix = "${project.name.replace("-", "_")}_"
}

repositories {
    flatDir {
        dir 'libs'
    }
}

dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation AndroidX.appcompat
    implementation AndroidX.core_ktx
    testImplementation Testing.jUnit
    androidTestImplementation Testing.androidx_junit
    androidTestImplementation Testing.espresso_core

    implementation Google.material
    implementation ThirdPart.Like.Component
    implementation Google.auto_service
    kapt Google.auto_service
}
```

    ③ service 模块
```groovy
apply plugin: 'kotlin-android'

android {
    compileSdkVersion BuildVersion.compileSdkVersion
    buildToolsVersion BuildVersion.buildToolsVersion

    defaultConfig {
        minSdkVersion BuildVersion.minSdkVersion
        targetSdkVersion BuildVersion.targetSdkVersion
        testInstrumentationRunner BuildVersion.AndroidJUnitRunner
    }

    buildTypes {
        release {
            consumerProguardFiles file('.').listFiles(new FilenameFilter() {
                @Override
                boolean accept(File file, String s) {
                    return file.extension == "pro"
                }
            })
        }
    }

    libraryVariants.all { variant ->
        variant.outputs.all { variantOutput ->
            outputFileName = "${project.name}-${variantOutput.name}-${defaultConfig.versionName}.aar"
        }
    }
}

dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation AndroidX.appcompat
    implementation AndroidX.core_ktx
    testImplementation Testing.jUnit
    androidTestImplementation Testing.androidx_junit
    androidTestImplementation Testing.espresso_core
}
```