## 功能介绍
1、提供了组件化的支持。

## 使用方法：

1、引用

在Project的gradle中加入：
```groovy
    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
```
在Module的gradle中加入：
```groovy
    dependencies {
        implementation 'com.github.like5188:Component:版本号'
    }
```

2、壳工程的 Application 继承 com.like.component.BaseComponentApplication。

3、组件的 Application 实现 com.like.component.IModuleApplication。

注意：

    ①、实现类必须要有一个 public 的无参构造函数，用于反射构造组件 Application 的实例。

    ②、必须在组件的 AndroidManifest.xml 文件中进行如下配置：
```java
    <meta-data
        android:name="实现类的全限定类名"
        android:value="IModuleApplication,priority" />

    其中:priority 表示初始化的优先级,值越大越先初始化.如果不填,那么默认为0,表示最低优先级.
```