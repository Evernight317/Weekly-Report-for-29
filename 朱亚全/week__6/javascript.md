
# JavaScript 学习笔记

## 概述
- **跨平台**：可在多种环境中运行（浏览器、Node.js 等）。
- **面向对象**：基于原型继承，支持对象、封装、继承等特性。

---

## 1. JS 的引入方式（与 HTML 结合）

### 内部脚本
- JavaScript 代码必须放在 `<script></script>` 标签内。
- 示例：
  ```html
  <script>
    console.log("内部脚本");
  </script>

### 外部脚本
- 外部 JS 文件只包含 JavaScript 代码，不包含 HTML 标签。
- 通过 src 属性引入。
- 示例
    ```html
    <script src="script.js"></script>

## 2. JS 的输出方式

| 方法 | 说明 |
|------|------|
| `window.alert()` | 弹出警告框 |
| `document.write()` | 向 HTML 文档写入内容 |
| `console.log()` | 输出到浏览器控制台 |

## 3. 变量

| 关键字 | 特点 |
|--------|------|
| `var` | 全局作用域、可重复声明 |
| `let` | 块级作用域、不可重复声明 |
| `const` | 声明常量，值不可改变（引用类型内容可修改） |

## 4. 数据类型与运算符
- 基本数据类型:
  `number、string、boolean、Null、NaN、undefined`
- 布尔转换（假值）:
 以下值转换为 `false`，其余为 `true`：
 `0、NaN、""、null、undefined`
- 比较运算符:
 `==`：会进行类型转换后再比较。
 `===`：不进行类型转换，要求值和类型都相等。

## 5. 流程控制语句
 与 Java 基本相同，包括：
 `if...else、switch、for、while、do...while、break、continue`

## 6. 函数

### 特点
- 不需要指定返回类型。

- 不需要指明参数类型。

- 参数个数可以任意（未传参数为 undefined）。

### 定义与调用

```javascript
function 函数名(参数列表) {
    // 函数体
}
函数名(实参);
```

## 7. JS 内置对象


### 1. Array（数组）

#### 定义
```javascript
let arr = [1, 2, 3];
```

#### 访问元素
```javascript
arr[index]  // 通过索引访问
```

#### 属性
| 属性 | 说明 |
|------|------|
| `length` | 获取数组长度 |

#### 常用方法

| 方法 | 说明 |
|------|------|
| `forEach(callback)` | 遍历数组（仅遍历有值的元素，与 `for` 遍历不同） |
| `push(element)` | 在末尾添加元素 |
| `splice(index, count)` | 从指定位置删除 `count` 个元素 |

##### 示例
```javascript
let arr = [1, 2, 3];

// forEach 遍历
arr.forEach(item => console.log(item));

// push 添加元素
arr.push(4);  // [1, 2, 3, 4]

// splice 删除元素
arr.splice(1, 1);  // 从索引1开始删除1个元素 → [1, 3, 4]
```

---

### 2. String（字符串）

#### 定义
```javascript
let str = "Hello World";
```

#### 属性
| 属性 | 说明 |
|------|------|
| `length` | 获取字符串长度 |

#### 常用方法

| 方法 | 说明 |
|------|------|
| `charAt(index)` | 获取指定位置的字符 |
| `indexOf(substring)` | 检索子串位置（返回索引，找不到返回 -1） |
| `trim()` | 去除字符串两侧空白 |
| `substring(start, end)` | 截取字符串 `[start, end)` |

##### 示例
```javascript
let str = "  Hello World  ";

str.charAt(0);        // " "
str.indexOf("World"); // 7
str.trim();           // "Hello World"
str.substring(0, 5);  // "Hello"
```

### 3. JS对象

#### 格式
```javascript
var 对象名 = {
	属性：值，
	函数名称：function(形参列表){}
};
// 实例
//自定义对象
var user = {
    name: "Tom",
    age: 10,
    gender: "男",
    // eat: function(){
    //     document.write("吃饭")
    // }
    eat(){
        document.write("吃饭");
    }
};
document.write(user.name);
user.eat();
```
#### 区分Json格式
- Json格式本身就是一个字典，在自定义对象的基础上把属性和函数也加上双引号。
- 包含在字符串内
- 相互转换
```javascript
var jsonStr = '{"name":"Tom","age":20,"addr":["北京","天津"]}';
alert(jsonStr);
//json转换为js对象
var _jsonStr = JSON.parse(jsonStr);
alert(_jsonStr["name"])

//js对象转换为stringify
var jsonStr_ = JSON.stringify(_jsonStr);
alert(jsonStr_);
```

### 4.浏览器对象：BOM


### 1. window 对象
> `window` 可以省略

#### 常用方法

| 方法 | 说明 |
|------|------|
| `alert()` | 弹出警告框 |
| `confirm()` | 确认操作，**有返回值**（true / false） |

---

### 2. 定时器

| 方法 | 说明 |
|------|------|
| `setInterval()` | 周期性执行函数 |
| `setTimeout()` | 延迟指定时间后执行一次 |

```javascript
//定时器
var i = 0;
//间隔XXms输出一条语句
setInterval(
    function(){
        i++;
        document.write(i+"   ");
    }, 3000//ms
)
//运行XX后输出语句，只输出一次
setTimeout(
    function(){
        document.write(50);
    },1000
)
```

---

### 3. location 对象

| 属性 | 说明 |
|------|------|
| `href` | 获取或设置当前页面 URL，**可实现页面跳转** |

#### 示例
```javascript
location.href = "https://example.com";  // 跳转到指定网址
```

### 5.文档对象DOM


#### 1. 获取页面元素（Document 对象）

| 方法 | 说明 |
|------|------|
| `document.getElementById()` | 通过 `id` 获取单个元素 |
| `document.getElementsByTagName()` | 通过标签名获取元素集合 |
| `document.getElementsByName()` | 通过 `name` 属性获取元素集合 |
| `document.getElementsByClassName()` | 通过类名获取元素集合 |

---

#### 2. JS 事件绑定（两种方式）

##### 方式一：通过 HTML 标签属性绑定
在标签中使用 `onclick` 属性直接绑定函数

**特点**：HTML 和 JS 混在一起，不利于维护。

```html
<button onclick="on()">点击</button>

<script>
    function on(){
        alert("按钮被点击了");
    }
</script>
```

##### 方式二：DOM 元素属性赋值

在 JS 中获取元素，然后给**元素的 对应的属性**（往往不同的元素有不同的属性）赋一个函数引用

**特点**：HTML 干净，JS 负责行为，结构和逻辑分离。

```javascript
<button id="btn">点击</button>

document.getElementById("btn").onclick = function() {
    alert("按钮被点击了");
};
```

##### 案例
```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8";
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>JS-事件-案例</title>
  </head>
  <body>
    <img id="light" src="img\\off.png"> <br>
    
    <!-- 要使灯泡变亮就要绑定事件 -->
    <input type="button" value="点亮" onclick="on()">
    <input type="button" value="熄灭" onclick="off()">

    <br>

    <input type="text" id="name" value="LITTLE" onfocus="lower()" onblur="upper()">

    <br> <br>

    <input type="checkbox" name="hobby"> 吃饭
    <input type="checkbox" name="hobby"> 睡觉
    <input type="checkbox" name="hobby"> 上课

    <br><br>

    <input type="button" value="全选" onclick="checkAll()"> 
    <input type="button" value="反选" onclick="reverse()"> 


    <script>
        function on(){
            //获取元素对象
            var state = document.getElementById("light");
            //设置src属性
            state.src = "img\\on.png";
        }

        function off(){
           var state = document.getElementById("light");
           state.src = "img\\off.png";
        }
        

        function lower(){
            var state = document.getElementById("name");
            state.value = "little";
        }
        function upper(){
            var state = document.getElementById("name");
            state.value = "LITTLE";
        }
        
        function checkAll() {
            var array = document.getElementsByName("hobby");
            for (let i = 0; i < array.length; i++) {
                const element = array[i];
                element.checked = true;
            }
        }
        function reverse() {
            var array = document.getElementsByName("hobby");
            for (let i = 0; i < array.length; i++) {
                const element = array[i];
                element.checked = false;
            }
        }

    </script>

  </body>
</html>
```