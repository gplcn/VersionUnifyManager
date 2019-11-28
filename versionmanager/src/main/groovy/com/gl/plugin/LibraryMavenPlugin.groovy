package com.gl.plugin

import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Exec

/**
 * Description :
 * Created by Peilei.Gao on 2019-11-27.
 */
class LibraryMavenPlugin implements Plugin<Project> {
    Project targetProject
    Project rootProject

    @Override
    void apply(Project target) {
        targetProject = target
        rootProject = target.rootProject
        target.apply plugin: 'maven'
        def gitAutoPull = target.tasks.create('gitAutoPull', Exec.class, new Action<Exec>() {
            @Override
            void execute(Exec task) {
                task.commandLine(['git', 'pull'])
            }
        })

        if (VersionManagerPlugin.gitAutoPullEnable) {
            target.tasks.getByName('preBuild').dependsOn(gitAutoPull)
        }

        target.uploadArchives() {
            repositories {
                mavenDeployer {
                    pom.groupId = rootProject.ext.PROJ_GROUPID
                    pom.artifactId = rootProject.ext.PROJ_ARTIFACTID
                    pom.version = getVersion()
                    repository(url: getUploadUrl()) {
                        authentication(userName: rootProject.ext.MAVEN_USERNAME, password: rootProject.ext.MAVEN_PASSWORD)
                    }
                }
            }
            outputs.upToDateWhen { false }
        }

        target.uploadArchives

    }

    def getVersion() {
        if ('SNAPSHOT' == getGrade("${rootProject.ext.PROJ_GROUPID}:${rootProject.ext.PROJ_ARTIFACTID}")) {
            getVersionName("${rootProject.ext.PROJ_GROUPID}:${rootProject.ext.PROJ_ARTIFACTID}") + "-SNAPSHOT"
        } else {
            getVersionName("${rootProject.ext.PROJ_GROUPID}:${rootProject.ext.PROJ_ARTIFACTID}")
        }
    }

    def getUploadUrl() {
        if ('SNAPSHOT' == getGrade("${rootProject.ext.PROJ_GROUPID}:${rootProject.ext.PROJ_ARTIFACTID}")) {
            rootProject.ext.MAVEN_SNAPSHOT_URL
        } else {
            rootProject.ext.MAVEN_RELEASE_URL
        }
    }


    def getVersionName(String group_artifact) {
        rootProject.customConfig.getVersionName(group_artifact)
    }

    def getGrade(String group_artifact) {
        rootProject.customConfig.getGrade(group_artifact)
    }
}
