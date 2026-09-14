# Git

## 一、git的实际应用意义

### 备份

### 版本控制

比如用git实现代码回滚。版本控制分为两种，集中式（SVN、CVS） 和分布式版本控制（Git）

git的原理：服务器端有一个共享版本库（远程仓库），而每个开发人员都有一个本地仓库。然后每次上传至远程仓库时，都会记录本地与远程的差异同时合并（当然，实际git的上传机制更加复杂）

不过，git实际上也可以开发人员之前相互push代码，但是一般不会这么做

### 协同开发（以及确定每个commit的负责人）

## 二、git的流程

git的核心包括 **远程仓库、本地仓库、暂存区、工作区** 四个模块

```Shell
clone  
checkout  
add  
commit  
fetch  
pull  
push
```

## 三、常用命令

linux 常用命令

```Shell
# 查看当前目录
ls或ll
# 查看文件内容
cat
# 创建文件
touch
# 进入vim编辑模式
vi
```

## 四、配置流程

设置用户信息

```Shell
# 配置用户名和邮箱
git config --global user.name"user.name"
git config --global user.email"user.email"
# 查看是否配置成功
git config --global user.name
git config --global user.email

# 配置常用指令的别名
# windows不一定允许用户创建点号开头文件
# 所以可以在git bash里创建
touch ~/.bashrc
# 然后在该文件中输入指令即可

# 解决git bash乱码问题（选配）
# 第一步，git bash执行
git config--global core.quotepath false
# 第二步，在${git_home}/etc/bash.bashrc 末尾加两行
export LANG="zh_CN.UTF-8"
export LC_ALL="zh_CN.UTF-8"
```

获取本地仓库

```Shell
# 对应文件夹打开git bash后输入
git init
# 出现Initialized empty Git repository in "文件路径"说明成功创建
```

## 五、文件状态转换

**未跟踪 Untracked** ：即新创建的文件，未被git记录；

**未暂存 Unstaged** ：已被git记录的文件，但在工作区中进行了修改，修改后的文件尚未被git记录；

以上两种状态，文件均位于工作区

```Shell
# 将工作区文件添加到暂存区，从而被git记录
git add . # .表示全部文件进入暂存区

# 将暂存区文件提交到本地仓库
git commit -m "" # -m ""即加上备注

# 查看修改状态（工作区+暂存区）
git status

# 查看提交日志
git log
# 同时，日志可以用如下参数进行自定义显示
--all # 显示所有分支
--pretty=oneline # 日志一行显示
--abbrev-commit # 缩短日志
--graph # 以图的形式显示

# 版本回退
git reset --hard commitID # commitID 可以用git log查看
# 即使cli页面清屏了，也可以用下面的命令看到已删除的commit记录
git reflog
```

对于工作区中的文件，如果不希望被git管理的话，可以用 `touch .gitignore` 命令创建   `.gitignore` 文件，并在文件中使用正则匹配等方式，即可过滤对应文件，使其不被git管理

## 六、分支

`head` 所指向的分支就是当前所在分支；由于 `git log` 同样也能显示分支，所以 `git log` 一定程度上能代替 `git branch`

分支合并是把一个其他分支合并到 **当前分支**  上，因此合并前要先切换到目标分支

```Shell
# 查看本地分支/当前所在分支
git branch
# 创建本地分支
git branch 分支名
# 切换分支
git checkout 分支名
# 创建并切换到不存在的分支
git checkout -b 分支名
# 分支合并
git merge 分支名
# 做检查后删除分支
git branch -d 分支名 # 无法删除当前所在分支
# 不做检查，直接删除分支
git branch -D 分支名
```

### 分支冲突

两个分支修改同一个文件并merge时，就会发生冲突。此时，git会同时保留两个分支的内容，head指向的就是当前分支内容，并用 `===` 分隔。

解决方法：打开文件手动处理冲突点，然后加入暂存区，并提交到本地仓库

### 实际流程&常用分支

`master/main` ：生产分支

`develop` ：

`feature` ：

`hotfix` ：
