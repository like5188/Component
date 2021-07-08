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

2、插件自动生成的 gradle 配置分为三种情况，分别如下：

① app 模块
```groovy
apply plugin: 'kotlin-android'
apply plugin: 'kotlin-kapt'

android {
    compileSdkVersion 30
    buildToolsVersion "30.0.3"
    defaultConfig {
        minSdkVersion 23
        targetSdkVersion 30
        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
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
    implementation "androidx.appcompat:appcompat:1.2.0"
    implementation "androidx.core:core-ktx:1.6.0"
    testImplementation "junit:junit:4.13.2"
    androidTestImplementation "androidx.test.ext:junit:1.1.3"
    androidTestImplementation "androidx.test.espresso:espresso-core:3.4.0"
    implementation "com.google.android.material:material:1.4.0"// 包含 androidx.constraintlayout

    implementation "com.github.like5188.Component:component:2.0.5"
    debugImplementation "com.squareup.leakcanary:leakcanary-android:2.7"
}
```

② module 模块
```groovy
apply plugin: 'kotlin-android'
apply plugin: 'kotlin-kapt'

android {
    compileSdkVersion 30
    buildToolsVersion "30.0.3"
    defaultConfig {
        minSdkVersion 23
        targetSdkVersion 30
        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
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
    implementation "androidx.appcompat:appcompat:1.2.0"
    implementation "androidx.core:core-ktx:1.6.0"
    testImplementation "junit:junit:4.13.2"
    androidTestImplementation "androidx.test.ext:junit:1.1.3"
    androidTestImplementation "androidx.test.espresso:espresso-core:3.4.0"
    implementation "com.google.android.material:material:1.4.0"// 包含 androidx.constraintlayout

    implementation "com.github.like5188.Component:component:2.0.5"
    implementation "com.google.auto.service:auto-service:1.0"
    kapt "com.google.auto.service:auto-service:1.0"
}
```

③ service 模块
```groovy
apply plugin: 'kotlin-android'

android {
    compileSdkVersion 30
    buildToolsVersion "30.0.3"
    defaultConfig {
        minSdkVersion 23
        targetSdkVersion 30
        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
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
    resourcePrefix = "${project.name.replace("-", "_")}_"
}

dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation "androidx.appcompat:appcompat:1.2.0"
    implementation "androidx.core:core-ktx:1.6.0"
    testImplementation "junit:junit:4.13.2"
    androidTestImplementation "androidx.test.ext:junit:1.1.3"
    androidTestImplementation "androidx.test.espresso:espresso-core:3.4.0"
}
```