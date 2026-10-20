# JavaScript

负责网页的行为（交互效果）

和Java是两个东西，是一门跨平合、面向对象的脚本语言。是用来控制网页行为的，它能使网页可交互

---

## 一、引入方式

将JS代码定义在HTML页面中

### 内部脚本

- JavaScript代码必须位于`<script></script>`标签之间
- 在HTML文档中，可以在任意地方，放置任意数量的`<script>` ；
- 一般会把脚本置于`<body>`元素的底部，可改善显示速度

### 外部脚本

将JS代码定义在外部JS文件中，然后引入到HTML页面中

- 外部JS文件中，只包含JS代码，不包含`<script>`标签；
- `<script>` 标签*不能自闭合* ；

## 二、基础语法

### 1.书写语法

### 2.变量

### 3.变量

用`var`声明变量；

- Javascript是一门弱类型语言，变量可以存放*不同类型的值* ；
- 命名规则基本和Java一致：组成字符可以是任何字母、数字、下划线 `_`或美元符号`$`，但*数字不能作为开头* ，同时建议驼峰命名；
- ECMAScript6新增了`let`关键字来定义变量。它的用法类似于`var`，但是所声明的变量，只在`let`*关键字所在的代码块内* 有效，且*不允许重复声明* ；
- ECMAScript 6 新增了`const` 关键字，用来声明一个**只读** 的常量。一旦声明，常量的值就不能改变；

### 4.数据类型、运算符、流程控制语句

#### 数据类型

分为**原始类型** 和**引用类型** ；通过`typeof()`可以获取数据类型；

- 对象为`null`时，返回的值是`Object`，此处null被认为是对象的占位符；

| 原始类型                                                                                                                                   |
| ------------------------------------------------------------------------------------------------------------------------------------------ |
| `number` 数字，整数、小数、NaN(Not a Number)<br />`string` 字符串，单双引号皆可                                                       |
| `boolean` true/false<br />`null` 对象为空<br />`undefined` 当声明的变量 **未初始化** 时，该变量的 **默认值** 是undefined |

#### 运算符

多了一个`===`全等运算符；

| `==`                                                   | `===`                                             |
| -------------------------------------------------------- | --------------------------------------------------- |
| 比较时会进行类型转换；<br />发现类型不同会先转换再比较； | 不会进行类型转换；<br />发现类型不同直接返回false； |

#### 类型转换

- 字符串类型转为数字：可以使用`parseInt()`方法将字符串字面值转为数字。如果字面值不是数字，则转为NaN；
- 其他类型转为boolean：

  - `Number`：`0`和`NaN`为false，其他均true；
  - `String`：空字符串为false，其他均true；
  - `null` 和 `undefined`：均false；

#### 流程控制语句

和java一致；

## 三、函数

通过`function` 定义；

```JavaScript
function functionName (参数1,参数2..) {
  
}
```

<script>标签不能自闭合

## 二、基础语法

### 1.书写语法

- 区分大小写，即变量名、函数名以及其他一切都区分大小写；
- 结尾分号可有可无；
- 注释的写法和Java一致，单行注释`//` 多行注释`/* */`文档说明`/** */` ；
- `{}` 表示代码块；

### 2.输出语句

一般用三种，写入对象也不同；

```JavaScript
<script>
// 浏览器弹出警告框
  	window.alert("hello world");

// 写入HTML，在浏览器展示
	document.write("hello world");

// 写入浏览器控制台
	console.log("hello world");
</script>

- 由于弱类型语言，所以形参不需要类型；
- 返回值也不需要定义类型，直接return即可；

```JavaScript
function add(a,b) {
  	return a+b;
}
```

还有一种定义方式

```JavaScript
var functionName=function(参数1,参数2..) {
  
}
var add=function(a,b) {
  	return a+b;
}
```

调用时，可以传多个参数，但是只接收前几个；

## 四、JS对象

分为三类：第一类Array String JSON；第二类`BOM`，浏览器对象模型；第三类`DOM`文档对象模型；（JS中标签其实都是封装成DOM对象）

### Array

#### 定义方式&访问

```JavaScript
// 方式一
var 变量名 = new Array(元素列表);
// 方式二
var 变量名 = [元素列表];

// 访问
arr[index] = value;
```

- JS中的数组相当于Java中的ArrayList；因此*长度可变，类型可变* 。具体表现：不存在数组索引越界的问题，元素默认值为 `undefined` ；

#### 遍历

与Java一致；

#### 属性

`arr.length` 返回数组长度；

#### 方法

|                                     |                                                             |
| ----------------------------------- | ----------------------------------------------------------- |
| `forEach()`                       | 遍历数组中的每个**有值** 的元素，并调用一次传入的函数 |
| `push()`                          | 将新元素添加到数组的末尾，并返回新的长度                    |
| `splice(startIndex, deleteCount)` | 从数组中删除元素                                            |

- for循环无论是否有值都遍历，但是forEach只遍历有值元素；
- ES6中，forEach方法提供了 **箭头函数** 简化，形式上很像Lambda；
- push方法中，可以一次性添加多个元素；

```JavaScript
arr.forEach(function (e) {
  	console.log(e);
})

// 箭头函数
// (形参) => { }
// var xxx = ( ) => { }
arr.forEach((e) => {
  	console.log(e);
})
```

### String

#### 创建方式

```JavaScript
// 方案一
var 变量名 = new String("...");
var s = new String("hello world");

// 方案二
var 变量名 = "...";
var s = "hello world";
```

#### 属性&方法

`String.length` 返回长度；

|                          |                                        |
| ------------------------ | -------------------------------------- |
| `charAt(index)`        | 返回指定位置字符                       |
| `indexOf(substring)`   | 检索子串的位置                         |
| `trim`                 | 去除字符串两侧的空格                   |
| `substring(start,end)` | 提取字符串中两个指定的索引号之间的字符 |

### 自定义对象

定义

```JavaScript
var 对象名 = {
  	属性名1:属性值1,
	属性名2:属性值2,
  	函数名称:function( ) { }
  	// 也可以省略
  	函数名称 () {}
}

var user = {
  	name : "Tom";
  	age : 20;
	// eat : function () {
 //      	alert("hello world");
 //    }
	eat () {
      	alert("hello world");
    }
}
```

调用格式

```JavaScript
// 对象名.属性名;
console.log(user.name);


// 对象名.函数名();
user.eat();
```

### 3.JSON

#### JSON本质

JSON是一段**文本** ，而程序中对文本的处理、存储方式就是通过*字符串* 形式；（网络中的传输流是字节流，而文本是先编码成字符串再传输的）

因此可以认为JSON就是**字符串** （在存储和传输时都是字符串，在后端程序中会被解析parse成JS对象）；

JSON可以用来传输复杂数据，比如说前后端需要传输一个可以封装成对象的复杂数据时（对象是无法直接传输的），就可以采用JSON，把对象转成一个字符串传输&解析；

---

全程JavaScript Object Notation，JavaScript对象标记法，JSON是通过JavaScript对象标记法书写的**文本** ；

基本按照自定义对象的方式，不过key要加上`""`；

现多用于作为*数据载体* ，在网络中进行数据传输；

```JSON
{
  	"name":"Tom",
    "age":20,
    "gender":"male"
}
```

#### 定义

```JSON
var 变量名 = '{"key1":value1, "key2":value2}';
```

value数据类型：1、数字（整数或浮点数）；2、字符串（用`""`）3、逻辑值(true/false)；4、数组（用 `[]`）5、对象（用`{}`）6、`null` ；

#### JSON字符串和JS对象的相互转换

```JavaScript
// JSON字符串转为JS对象
var jsObject = JSON.parse(userstr);

// JS对象转为JSON字符串
var jsonStr = JSON.stringify(jsObject);
```

### 4.BOM

Browser Object Model浏览器对象模型，允许JavaScript与浏览器对话，JavaScript将浏览器的各个组成部分封装为对象；

`Window` 浏览器窗口对象
`Navigator` 浏览器对象
`Screen` 屏幕对象
`History` 历史记录对象
`Location` 地址栏对象

#### Window

对象获取：直接使用`window`，其中`window.`可以省略；

属性
`history`：对History 对象的只读引用。
`location`：用于窗口或框架的Location对象。
`navigator`：对 Navigator 对象的只读引用。

方法

`alert()`：显示带有一段消息和一个确认按钮的警告框。

`confirm()`：显示带有一段消息以及确认按钮和取消按钮的对话框；confirm实际上是有返回值的，为boolean；

`setinterval()`：按照指定的周期（以毫秒计）来调用函数或计算表达式

`setTimeout()`：在指定的毫秒数后调用函数或计算表达式；只会执行一次；

```JavaScript
setInterval (function () {
  	i++;
  console.log(i);
},2000);

setTimeout(function () {
  	alert("JS");
},3000);
```

#### Location

地址栏对象；
使用`window.location`获取，其中`window.`可以省略；

属性：

`href`：设置或返回完整的URL `location.href`即获得地址栏URL

`location.href = ""`即可设置地址栏的URL并自动跳转；

### DOM

Document Object Model，文档对象模型；将标记语言的各个组成部分封装为对应的对象：
`Document` 整个文档对象
`Element` 元素对象 （标签中的元素）
`Attribute` 属性对象 （标签中的属性）
`Text` 文本对象 （标签内的文本）
`Comment` 注释对象

在解析html后将html文档的各个部分封装成对象；DOM树；JS通过DOM和时间监听机制控制网页行为；

JavaScript 通过DOM，就能够对HTML进行操作：

- 改变HTML元素的内容
- 改变HTML元素的样式（CSS）
- 对HTMLDOM 事件作出反应
- 添加和删除HTML 元素

DOM是W3C的标准，定义了访问HTML和XML文档的标准，分为3个不同的部分：`Core DOM`/`XML DOM`/`HTML DOM`

#### Core DOM

所有文档类型的标准模型
`Document`:整个文档对象

`Element`:元素对象

`Attribute`：属性对象

`Text`:文本对象

`Comment`：注释对象

#### XML DOM

XML文档的标准模型

#### HTML DOM

HTML文档的标准模型

Image:`<img>`

Button：`<input type = 'button'>`

HTML中的Element对象可以通过Document对象获取，而Document对象是通过window对象获取的；

1. 根据id属性值获取，返回单个Element对象
2. 根据标签名称获取，返回Element对象数组
3. 根据name属性值获取，返回Element对象数组
4. 根据class属性值获取，返回Element对象数组

```JavaScript
var h1= document.getElementById('h1');
var divs = document.getElementsByTagName('div');
var hobbys = document.getElementsByName('hobby');
var clss=document.getElementsByClassName('cls');
```

改变元素内容：

使用`innerHTML`方法

```JavaScript
var divs=document.getElementsByClassName('cls');
var divs = divs[0];
divl.innerHTML= "123";
```

### JS事件监听

事件：HTML事件是发生在HTML元素上的事情；

事件监听：JavaScript可以在事件被侦测到时执行代码；

#### 事件绑定

方式一：通过HTML标签中的事件属性进行绑定；

方式二：通过DOM元素属性绑定；

#### 常见事件

|             |                          |
| ----------- | ------------------------ |
| `onclick` | 鼠标单击事件             |
| onblur      | 元素失去焦点             |
| onfocus     | 元素获得焦点             |
| onload      | 某个页面或图像被完成加载 |
| onsubmit    | 当表单提交时触发该事件   |
| onkeydown   | 某个键盘的键被按下       |
| onmouseover | 鼠标被移到某元素之上     |
| onmouseout  | 鼠标从某元素移开         |
