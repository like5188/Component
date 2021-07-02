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
        classpath "com.github.like5188:Dependencies:0.1.5"
        classpath "com.like:component-plugin:版本号"
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
```

    ② module 模块
```groovy
```

    ③ service 模块
```groovy
```