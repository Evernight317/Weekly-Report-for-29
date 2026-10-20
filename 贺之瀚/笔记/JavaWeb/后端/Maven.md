# Maven

依赖管理，统一项目结构，项目构建；

Maven基于项目对象模型(POM)的概念，通过一小段描述信息来管理项目的构建；

## Maven模型

POM，依赖管理模型，构建生命周期/阶段；

仓库：用于存储资源，管理各种jar包，分为本地仓库、中央仓库、远程仓库（私服）；先找本地再找远程再中央；如果远程也没有就从中央下载到远程；

## Maven项目创建

## Maven项目导入

方式一：+ -> 选择pom.xml

## 依赖管理

### 依赖配置

指当前项目运行所需要的jar包，一个项目中可以引入多个依赖；

1. 在pom.xml 中编写`<dependencies>`标签；
2. 在`<dependencies>`标签中使用 `<dependency>` 引入坐标；
3. 定义坐标的`groupId` `artifactId` `version`；
4. 配置完后要重新加载才能生效；

```XML
<dependencies>
	<dependency>
      	<groupId>...</groupId>
      	<artifactId>...</artifactId>
      	<version>...</version>
    </dependency>
</dependencies>
```

如果引入的依赖在本地仓库不存在，将会连接远程仓库中央仓库，然后下载依赖（这个过程会比较耗时，耐心等待）；

如果不知道依赖的坐标信息，可以到`https://mvnrepository.com/`搜索；

### 依赖传递

依赖有传递性；

- 直接依赖：在当前项目中通过依赖配置建立的依赖关系；
- 间接依赖：被依赖的资源如果依赖其他资源，当前项目间接依赖其他资源；

#### 排除依赖

排除依赖指主动断开依赖的资源，被排除的资源 *无需指定版本* ；

```XML
<!-- 在直接依赖的位置排除间接依赖 -->
<exclusions>
  	<exclusion>
  		<groupId></groupId>
  		<artifactId></artifactId>    
    </exclusion>
</exclusions>
```

### 依赖范围

依赖的jar包，默认情况下，可以在任何地方使用。可以通过`<scope></scpoe>`设置其作用范围；

#### 作用范围

- 主程序范围有效。（main文件夹范围内）
- 测试程序范围有效。（test文件夹范围内）
- 是否参与打包运行。（package指令范围内）

默认都可以；

#### scope常见取值

|                 | main | test | package |
| --------------- | ---- | ---- | ------- |
| compile（默认） | √   | √   | √      |
| test            |      | √   |         |
| provided        | √   | √   |         |
| runtime         |      | √   | √      |

### 生命周期

对所有的maven项目构建过程进行抽象和统一；

Maven中有3套**相互独立** 的生命周期：

- clean：清理工作，~清理上一次构建产生的文件~ ；
- default：核心工作，如：编译、测试、打包、安装、部署等；
- site：生成报告、发布站点等；

每套生命周期包含一些阶段（phase），阶段是有顺序的，后面的阶段依赖于前面的阶段；即在**同一套** 生命周期中，当运行后面的阶段时，前面的阶段都会运行（但是前面的阶段也可以手动跳过）

#### 常见生命周期阶段

clean属于clean周期，剩下四个都属于default周期；

- clean：移除上一次构建生成的文件
- compile：编译项目源代码
- test：使用合适的单元测试框架运行测试(junit)
- package：将编译后的文件打包，如：jar、war等
- install：将打包好的包安装到本地仓库

#### 执行指定生命周期的方式

- 在maven工具栏中，选中对应的生命周期，双击执行；
- 命令行中通过命令执行，如`mvn compile`；
