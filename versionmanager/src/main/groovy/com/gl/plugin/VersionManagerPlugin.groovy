package com.gl.plugin


import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.DependencyResolveDetails

/**
 * Description :
 * Created by Peilei.Gao on 2019-11-18.
 */
class VersionManagerPlugin implements Plugin<Project> {
    static Boolean gitAutoPullEnable = false
    @Override
    void apply(Project project) {
        def rootProject = project.rootProject
        def expectFile = null
        def localPropertiesDirEnable = true
        def customConfig = rootProject.getExtensions().create("customConfig", CustomConfig)
        File file = rootProject.file('local.properties')
        if (file.exists()) {
            InputStream inputStream = rootProject.file('local.properties').newDataInputStream();
            Properties properties = new Properties()
            properties.load(inputStream)

            if (properties.containsKey("version_manager_dir")) {
                expectFile = new File("${properties.getProperty("version_manager_dir")}/version-config.xml")
            }

            if (properties.containsKey("version_manager_dir_enable")) {
                localPropertiesDirEnable = properties.getProperty("version_manager_dir_enable") asBoolean()
            }
            if (properties.containsKey("git_auto_pull_enable")) {
                gitAutoPullEnable = Boolean.parseBoolean(properties.getProperty("git_auto_pull_enable") )
            }
        }
        if (null == expectFile || !localPropertiesDirEnable || !expectFile.exists())
            expectFile = new File("${project.getRootDir().parentFile.path}/version-config.xml")
        if (!expectFile.exists()) expectFile = new File("${project.getRootDir().path}/version-config.xml")
        println("version-config.xml path is ${expectFile.path}")
        def slurper = new XmlSlurper().parse(expectFile)
        def androidNode = slurper.android[0]
        customConfig.androidConfig = ['compileSdkVersion': androidNode.compileSdkVersion[0].text(),
                                      'minSdkVersion'    : androidNode.minSdkVersion[0].text(),
                                      'targetSdkVersion' : androidNode.targetSdkVersion[0].text()]

        def externalDependencies = customConfig.externalDependencies = [:]
        slurper.external[0].group.each {
            def groupId = it.id[0].toString()
            it.artifact.each {
                def artifactID = it.id[0].toString()
                def artifactVersionName = it.version_name[0].toString()
                externalDependencies.put("${groupId}:${artifactID}".toString(),
                        new ExDependenciesConfig(dependencyNotation: "${groupId}:${artifactID}:${artifactVersionName}",
                                groupID: groupId,
                                artifactID: artifactID,
                                versionName: artifactVersionName))
            }
        }

        def internalDependencies = customConfig.internalDependencies = [:]
        def globalGrade = slurper.internal[0].global_grade[0].toString()
        if (!(globalGrade in ["CUSTOM", "SNAPSHOT", "RELEASE"])) {
            throw new IllegalStateException("!!! <global_grade> current value is ${globalGrade} out of range： [CUSTOM,SNAPSHOT,RELEASE]")
        }
        slurper.internal[0].group.each {
            def groupId = it.id[0].toString()
            it.artifact.each {
                def artifactGrade = it.grade[0].toString()
                if (!(artifactGrade in ["CUSTOM", "SNAPSHOT", "RELEASE"])) {
                    throw new IllegalStateException("!!! <grade> inside <internal> current value is ${artifactGrade} out of range： [SNAPSHOT,RELEASE]")
                }
                def resultGrade = "SNAPSHOT".equals("CUSTOM".equals(globalGrade) ? artifactGrade : globalGrade) ? "-SNAPSHOT" : ""
                def artifactID = it.id[0]
                def artifactVersionName = it.version_name[0]
                internalDependencies.put("${groupId}:${artifactID}".toString(),
                        new InDependenciesConfig(dependencyNotation: "${groupId}:${artifactID}:${artifactVersionName}${resultGrade}",
                                groupID: groupId,
                                artifactID: artifactID,
                                versionName: artifactVersionName,
                                grade: "CUSTOM".equals(globalGrade) ? artifactGrade : globalGrade))
            }
        }

        project.rootProject.allprojects {
            configurations.all {
                resolutionStrategy.cacheDynamicVersionsFor 5, 'minutes'
                resolutionStrategy.cacheChangingModulesFor 0, 'seconds'
                resolutionStrategy {
                    internalDependencies.values().each {
                        force it.dependencyNotation
                    }
                }
            }
        }
    }

}
