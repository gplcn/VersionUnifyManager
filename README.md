# 组件化版本统一管理插件***version-manager***

## 引用步骤

```
 准备工作：
 1 新建一个目录并创建文件「version-config.xml」按规范编写相应信息
 2 将「version-config.xml」复制一个副本到项目的根目录下	
 3 在项目的「local.properties」中添加上述文件所在目录信息
 	eg:
 	version_manager_dir= ${version-config.xml的目录路径}
	version_manager_dir_enable = true  

	version_manager_dir_enable 取值true或false 含义为是否使用上述配置路径的中的配置文件
	如果为false 将引用项目根目录下的配置文件「version-config.xml」
	
```

1. 在 RootProject 的「build.gradle」中的dependencies添加：``classpath 'com.gl.plugin:version-manager:1.0.0'``

eg:

```
buildscript ｛
    repositories {
        google()
        jcenter(）
    }
    dependencies {
        ．．．
        classpath 'com.gl.plugin:version-manager:1.0.0'
    }
}

```

2. 在 RootProject 的「build.gradle」中应用插件：

	``apply plugin: 'versionManagerPlugin'``
	*  注： 不可以添加到buildscript前面
	*  注： 只能添加到RootProject的build.gradle中，Module的build.gradle不可添加
3. 在Module的「build.gradle」应用插件：

	``apply plugin: 'versionResolvePlugin'``
4. 在需要引入配置文件中版本的地方调用对应API引入对应的版本控制信息
	

## ***API***
* customAndroidConfig(String propertyName)：获取Android编译配置版本信息
	
	eg:``customAndroidConfig('compileSdkVersion')``
* versionName(String group_artifact) :获取当前工程的版本信息
	
	eg:
	``versionName("com.abc.module:lib1")``
	
	在「gradle.properties」中配置好groupid和artifactid可以这样使用：
	``versionName("${PROJ_GROUP}:${PROJ_ARTIFACTID}")``

* grade(String group_artifact)：获取当前工程的版本级别
	
	eg:
	``grade("com.abc.module:lib1")``
	
	在「gradle.properties」中配置好groupid和artifactid可以这样使用：

	``grade("${PROJ_GROUP}:${PROJ_ARTIFACTID}")``
* notation(String group_artifact)：获取需要依赖信息
   
   eg:``implementation notation('com.google.code.gson:gson')``
   

###### ***APIDemo***
  一个module的 build.gradle文件：
  
   ```
apply plugin: 'com.android.library'
apply plugin: 'kotlin-android'
apply plugin: 'kotlin-kapt'
apply plugin: 'kotlin-android-extensions'
apply plugin: 'versionResolvePlugin'

ext {
    customVersionName = versionName("${PROJ_GROUP}:${PROJ_ARTIFACTID}")
    SNAPSHOTS = 'SNAPSHOT'.equalsIgnoreCase(grade("${PROJ_GROUP}:${PROJ_ARTIFACTID}"))
}

android {
    compileSdkVersion customAndroidConfig('compileSdkVersion')
    defaultConfig {
        minSdkVersion customAndroidConfig('minSdkVersion')
        targetSdkVersion customAndroidConfig('targetSdkVersion')
        versionCode 1
        versionName customVersionName
        testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
        javaCompileOptions {
            annotationProcessorOptions {
                arguments = [AROUTER_MODULE_NAME: "inlibone"]
            }
        }
    }


    kapt {
        arguments {
            arg("moduleName", project.getName())
        }
        generateStubs = true
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }

    compileOptions {
        sourceCompatibility = '1.8'
        targetCompatibility = '1.8'
    }
}

dependencies {
    implementation fileTree(include: ['*.jar'], dir: 'libs')
    implementation notation ('com.android.support:appcompat-v7')
    implementation notation('com.google.code.gson:gson')
}

   ```






inlibone  	|	1.0		|1.0S	|2.0		|2.0S
:---|:--|:--|:--|:--
inlibtwo |1.0（one1.0）|1.0S->(one1.0S)|2.0(one1.0)|2.0S(one1.0S)





``gradlew -q :app:dependencies > dependencies.txt``


