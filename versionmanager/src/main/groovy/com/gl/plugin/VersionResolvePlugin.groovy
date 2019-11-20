package com.gl.plugin


import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Description :
 * Created by Peilei.Gao on 2019-11-18.
 */
class VersionResolvePlugin implements Plugin<Project> {
    Project project
    @Override
    void apply(Project project) {
        project.ext.customAndroidConfig = this.&getCustomAndroidConfig
        project.ext.notation = this.&getStandardDependencyNotation
        project.ext.versionName = this.&getVersionName
        project.ext.grade = this.&getGrade
        this.project = project.rootProject
    }

    def getCustomAndroidConfig(String key) {
        project.customConfig.getCustomAndroidConfig(key)
    }

    def getStandardDependencyNotation(String path) {
        project.customConfig.getStandardDependencyNotation(path)
    }

    def getVersionName(String group_artifact) {
        project.customConfig.getVersionName(group_artifact)
    }

    def getGrade(String group_artifact) {
        project.customConfig.getGrade(group_artifact)
    }
}
