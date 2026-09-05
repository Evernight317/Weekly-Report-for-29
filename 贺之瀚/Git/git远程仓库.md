# git远程仓库

常用的远程仓库：github、gitlab；gitlab一般是git私服，github一般是开源项目平台

SSH配置官方文档：[Github配置SSH](https://docs.github.com/zh/authentication/connecting-to-github-with-ssh)

---


## 一、SSH&配置SSH

SSH，即Secure Shell(安全外壳协议) `ssh key` 分为公钥和私钥

### 生成SSH key



## 二、远程仓库

### 创建远程仓库

```Shell
# 1.添加远程仓库
# 先初始化本地库，然后与远程仓库连接
git remote add origin <仓库URL> # 远端名称默认origin
# 查看远程仓库
git remote
# 2.推送到远程仓库
# 完整命令如下
git push [-f] [--set-upstream] [远端名] [本地分支名]:[远端分支名]
-f # 强制覆盖 
--set-upstream # 推送到远端的同时并且建立起和远端分支的关联关系。
# 简略版
git push origin main/master
# 当前分支名和远端名已关联，则可进一步省略
git push

# 查看本地仓库与远程仓库的详细关系
git branch -vv
```

### 从远程仓库克隆

对应文件夹打开 `git bash` 后输入 $，仓库名默认远端同名，同时URL个人选择SSH

```Shell
git clone <仓库URL> [本地仓库名]
```

### 获取更新

抓取 `fetch` 和拉取 `pull` ，前者抓取但不同步，需要额外执行 `merge`

```Shell
git fetch [远端名] [本地名]

git merge [远端名] [本地名]

git pull [远端名] [本地名]
```

### 远程冲突

先git pull，相当于本地解决冲突
