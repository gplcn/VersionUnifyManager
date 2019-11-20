package com.gl.plugin;

/**
 * Description :
 * Created by Peilei.Gao on 2019-11-18.
 */
class ExDependenciesConfig {
    String dependencyNotation
    String groupID
    String artifactID
    String versionName

    @Override
    String toString() {
        return "ExDependenciesConfig{" +
                "dependencyNotation='" + dependencyNotation + '\'' +
                ", groupID='" + groupID + '\'' +
                ", artifactID='" + artifactID + '\'' +
                ", versionName='" + versionName + '\'' +
                '}';
    }
}
