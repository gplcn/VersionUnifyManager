package com.gl.plugin

import org.gradle.api.Project;

/**
 * Description :
 * Created by Peilei.Gao on 2019-11-18.
 */
class CustomConfig {
    Map<String,String> androidConfig
    Map<String,InDependenciesConfig> internalDependencies
    Map<String,ExDependenciesConfig> externalDependencies

    def getCustomAndroidConfig(String key){
        androidConfig.get(key) as int
    }

    def getVersionName(String group_artifact){
        internalDependencies.get(group_artifact).versionName
    }
    def getGrade(String group_artifact){
        internalDependencies.get(group_artifact).grade
    }

    def getStandardDependencyNotation(String path) {
        def result = externalDependencies.get(path)
        result = null == result ?
                internalDependencies.get(path).dependencyNotation
                : result.dependencyNotation
    }
}
