package com.gl.plugin;

/**
 * Description :
 * Created by Peilei.Gao on 2019-11-18.
 */
class InDependenciesConfig {
    String dependencyNotation
    String groupID
    String artifactID
    String versionName
    String grade


    @Override
    String toString() {
        return "InDependenciesConfig{" +
                "dependencyNotation='" + dependencyNotation + '\'' +
                ", groupID='" + groupID + '\'' +
                ", artifactID='" + artifactID + '\'' +
                ", versionName='" + versionName + '\'' +
                ", grade='" + grade + '\'' +
                '}';
    }
}
