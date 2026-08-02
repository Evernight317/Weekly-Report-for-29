# C++开发环境搭建学习周报
## 一、本周完成工作
### 1. MinGW 编译器部署
1. 初期下载 MingW 源码压缩包，源码无法直接使用，清理文件后更换预编译的`.7z`程序包
2. 解压文件放置路径：`D:\Software\mingw-w64-gcc-13.2-stable-r40`
3. 系统环境变量 `Path` 添加 `D:\Software\mingw-w64-gcc-13.2-stable-r40\bin`
4. CMD 执行 `gcc -v` 校验，编译器系统识别正常，底层编译环境搭建完成

### 2. VSCode 编辑器配置优化
1. 安装 `C/C++`、`C/C++ Compile Run` 运行插件，首次运行提示找不到编译器，重启软件后解决配置刷新问题
2. 代码编译运行正常，但中文输出出现乱码，修改用户`settings.json`配置终端与文件编码为UTF-8
```json
{
    "terminal.integrated.defaultProfile.windows": "PowerShell",
    "terminal.integrated.profiles.windows": {
        "PowerShell": {
            "source": "PowerShell",
            "args": ["-NoExit", "-Command", "[Console]::OutputEncoding = [System.Text.Encoding]::UTF8"]
        }
    },
    "files.encoding": "utf-8",
    "editor.fontSize": 25,
    "redhat.telemetry.enabled": true,
    "workbench.colorTheme": "Light+",
    "code-runner.runInTerminal": true,
    "files.autoGuessEncoding": true,
    "chat.disableAIFeatures": true
}
```
3. 区分系统默认配置文件（只读锁定）与用户自定义配置文件，使用用户JSON文件完成自定义修改

### 3. C++ 基础代码实操练习
1. 使用标准头文件 `<iostream>` 编写基础输出代码，跑通编译-生成exe-运行整套流程
2. 测试万能头文件 `<bits/stdc++.h>`，编辑器存在红线警告，但不影响程序实际编译运行
3. 编写中文输出测试代码，等待编码配置生效验证效果
```cpp
#include <iostream>
using namespace std;
int main()
{
    cout << "成功" << endl;
    return 0;
}
```

## 二、问题汇总与解决办法
| 出现问题 | 产生原因 | 解决方案 |
| ---- | ---- | ---- |
| 下载源码包无法调用gcc命令 | 源码包需要手动编译，新手无法直接使用 | 更换官网预编译好的7z程序包 |
| VSCode弹窗提示未找到编译器 | 环境变量修改后编辑器没有重载系统配置 | 完全关闭VSCode软件后重新打开 |
| 运行程序中文输出乱码 | Windows终端默认编码GBK，程序文件为UTF-8 | 在settings.json强制终端输出编码为UTF-8 |
| bits/stdc++.h文件标红报错 | C/C++插件未配置头文件检索路径 | 现阶段可忽略警告，不影响代码运行 |
| defaultSettings.json提示只读无法编辑 | 默认配置受系统保护禁止修改 | 选择「打开用户设置(JSON)」编写配置 |

## 三、本周学习收获
1. 熟练掌握 Windows 平台 MinGW + VSCode 全套C++开发环境搭建流程，理解环境变量的加载原理
2. 掌握VSCode JSON配置文件语法，能够自主调整终端样式、编码、运行插件等参数
3. 分清**编译运行报错**和**编辑器语法提示红线**两种不同提示，不会被编辑器警告干扰程序编写
4. 掌握C++最基础的控制台输出语法，具备编写、调试简单测试程序的能力
