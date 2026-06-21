# Weekly-Report 2026

## 25届NEUQ-ACM技术部周报收集仓库

### 前置工作  
1. 配置自己的SSH密钥，参考文档 [GitHub 配置SSH](https://docs.github.com/zh/authentication/connecting-to-github-with-ssh)   
2. 将远程仓库fork成个人仓库：`fork` -> `create fork`  
3. 把你fork的仓库拉到到本地（用ssh进行克隆）

### 周报上传  
1. 写周报之前，需要做以下同步（注意这里一定不要有修改）
   1. 找到你fork下来的仓库，点击`sync fork`按钮，实现个人仓库和远程仓库的同步（如果没有按钮，意味着个人仓库和远程仓库已经同步了）
   2. 在本地文件夹的目录下，执行`git pull origin master`，更新本地文件夹
2. 创建以你的名字命名的文件夹，在文件夹中创建若干个以`week__`命名的文件夹，每周将周报和自己做的成果放入当前周的文件夹中   
```
文件结构：
文件夹（自己名字命名）
  |- week__
    |- 你的成果
    |- 周报.md
```   
3. 修改完毕，现提交修改（两种方式均可）
   1. 终端操作：`git add .`-> `git commit -m "xxx"` -> `git push origin master`
   2. UI操作：IDE左侧栏Commit中选中本次提交的Change，并命名 -> 点击`Commit and Push` -> 点击`Commit Anyway and Push` (点击`Commit`，再从IDE上边栏中找到`Git` -> `Push`) -> 这里会出现一个界面，会显示你的Changes和push到的分支 -> 点击`Push`
4. 至此已经完成了本地仓库和个人仓库的同步，成功将本地的修改上传到了你fork的仓库
5. 在github个人仓库的上侧栏点击`Pull requests` -> 点击`New pull request` -> 选择将你的个人仓库`pull`到远程仓库 -> 点击`Create pull request`创建一个pr -> 给pr命名，然后提交

### 周报要求
- 本届周报不强制上交，但是作为签到标准，如果发现三周未交且无特殊情况反映到ghy，视作退出本届技术部，不定期移除群成员
- 考虑到同学们学习进度与方向不一致，不强制学习java开发路线，每周周报可以上交多元化的学习成果，确保个人技术持续增长
- 未完待续...

### 其他  
- git指令可以参考 [郭泓邑[著] -- Git使用指南](docs/Git使用指南.md)，另外建议大家多学一些git指令以应对多种情况[git在线学习](https://learngitbranching.js.org/?locale=zh_CN)
- 本届技术部将在26年3月中旬选定一日进行集中面试，筛选成员加入不洗碗工作室
