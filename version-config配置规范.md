# 组件化版本统一管理插件***version-config.xml***配置规范

```
<?xml version="1.0" encoding="utf-8" ?>
<config>
    <!-- Android build&compile info  -->
    <android>
        <compileSdkVersion>27</compileSdkVersion>
        <minSdkVersion>15</minSdkVersion>
        <targetSdkVersion>23</targetSdkVersion>
    </android>

    <!-- 内部组件依赖 -->
    <internal>
        <!-- global_grade value can be:[CUSTOM,SNAPSHOT,RELEASE] ; grade value can be:[SNAPSHOT,RELEASE]-->
        <global_grade>RELEASE</global_grade>
        <group>
            <id>com.abc.module</id>
            <!--test 1 -->
            <artifact>
                <id>inlibone</id>
                <version_name>2.0</version_name>
                <grade>SNAPSHOT</grade>
            </artifact>

            <!--test 2 -->
            <artifact>
                <id>inlibtwo</id>
                <version_name>2.0</version_name>
                <grade>SNAPSHOT</grade>
            </artifact>
        </group>

    </internal>

    <!-- 外部依赖 -->
    <external >

        <!-- V7 -->
        <group >
            <id>com.android.support</id>
            <artifact>
                <id>appcompat-v7</id>
                <version_name>27.1.1</version_name>
            </artifact>
        </group>

        <group>
            <id>com.fasterxml.jackson.core</id>
            <artifact>
                <id>jackson-core</id>
                <version_name>2.9.7</version_name>
            </artifact>
        </group>
        
    </external>
</config>
```