# API

即应用程序编程接口，`jdk` 自带的API 类似于 `C++` 的库函数，可以被直接调用

## Scanner等自带api使用

以下三步：1. 导包；2. 得到键盘扫描器对象；3. 等待接受输入数据，即调用成员方法；

---

## 一、包

类似于文件夹，用来管理维护程序。一般要求先建包再建类，IDE会自动建包、建类

```Java
// 建包语法，确定编译后的程序存放路径
package Noteeee.note // 路径中的\用.代替
```

1. 同一个包下的程序，可以直接访问；
2. 当前程序中访问其他包下的程序，必须要 **先导包** 才可以访问 `import` ， `import 包名.类名` 不过IDE中，导包命令会自动生成。
3. 自己的程序中调用Java提供的程序，也需要先导包才可以使用；注意：`Java.lang` 包不需要导包
4. 访问多个其他包下的程序，这些程序名又一样的情况下，默认只能导入一个程序，另一个程序必须 **带包名和类名** 来访问

```Java
com.itheima.pkg.itcast.Demo2 // 另一个程序要求包名类名全部给出
```

## 二、String

### 创建对象并封装字符串

```Java
// 方法一 ""
String s = "abc";
// 方法二 使用构造器初始化对象
public String ();
public String (Str);
public String (char []); // 字符数组
public String (byte []); // 字节数组，即ASCII码
```

`String` 对象的名称本身存储的是堆中地址，但

### 常用方法

```Java
public int length() // 字符串长度
public char charAt(int index) // 获取对应位置的char
public char[] toCharArray() // 将String转换成char数组
// 两个字符串比较函数
public boolean equals(Object anObject) // 判断两个字符串是否相同，相同返回true
public boolean equalsIgnoreCase(String anotherString) // 同上，但忽略大小写

// substring的两种重载，区间左开右闭，尾参未给出则默认至串尾
public String substring(int beginIndex, int endIndex) // 根据两个索引截取新字符串
public String substring(int beginIndex) // substring，且截到字符串末尾

public String replace(CharSequence target, CharSequence replacement) // 把字符串中的target子串替换成新子串
public boolean contains(CharSequence s) // 判断是否包含特定字符串
public boolean startsWith(String prefix) // 判断前缀是否为特定字符串，是返回true
public String[] split(String regex) // 把字符串按照特定字符串内容分割，并返回字符串数组 即遇到regex截断一次
```

### 注意

1. java中的String对象无法直接用 `[]` 调用具体字符，需要使用 `charAt(int index)` 函数；
2. String对象是 **不可变字符串对象**，而对于下面这个，实际上是重新创建了一个新的String对象，并且把新地址赋给 `s1`，并且这种方式得到的字符串存储在堆区中

```Java
String s1 = "hello";
String s2 = " world";
s1 += s2;
```

3. 只要是以“...”方式写出的字符串对象，会存储到 **字符串常量池** ，且相同内容的字符串只存储一份；但如果是通过 `new` 动态分配的话，每次都会产生一个新的对象存储在堆中
4. Java存在编译优化机制，程序在编译时：`"a"+"b"+"c"` 会直接转成 `"abc"` ，以提高程序的执行性能

```Java
String s1 = "123";
String s2 = "123"; // s2不会被存储，而是直接指向s1
```

同时，`java` 中`String` 没有重载 `=` ，因此直接比较字符串变量名的话，相当于比较两者地址是否相同，一般均为 `false`；故比较内容应用上方的 `equals` `equalsIgnoreCase` 函数

```Java
String s1 = "123";
String s2 = "123";
System.out.println (s1 == s2); // false
System.out.println (s1.equals (s2)); // true
```

## 三、ArrayList

### 集合

类似于 `C++`中的 `vector` ，即长度可变且会自动扩容的动态数组；

`ArrayList` 是最常见的一种集合。`ArrayList` 在没有指定元素类型的情况下，可以 **存储任意种类变量** ，（但感觉这个特性很不好用，慎用）；在指定元素类型后，就和 `vector` 很像了

### 构造器&常用方法

1. 和 `String` 一样，没有重载运算符，所以需要通过 `get` 函数获取其中元素；
2. 此处没有 `int` 和 `size_t` 区分
3. 和String一样，`System.out.print (list)` 会 **直接输出整个 `list`** ，

```Java
ArrayList () // 容量为10的空列表
ArrayList(int initialCapacity) // 构造特定容量的空列表
ArrayList(collection <extends E> c) // 只能存储特定类型元素
------------------------

public boolean add(E e) // 将元素添加到末尾
public void add(int index,E element) // 在指定位置插入指定元素
public E get(int index) // 返回指定位置元素
public int size() // 返回元素个数/长度
public E remove(int index) // 删除指定位置元素，并将其返回
public boolean remove(Object o) // 删除指定位置元素，返回是否删除成功
public E set(int index,E element) // 修改指定位置元素，返回被修改的元素
```

## 杂记

`Ex\Food`这一案例中，关于 `Scanner` 类有个问题。`Scanner` 创建对象时，形参是 `System.in`，即系统输入流，而 `Scanner.close ()` 会把底层输入流一同关闭。因此，实际使用中，只需在整个程序结束时调用一次 `Scanner.close ()` 即可，否则会出现 `scanner` 类想读取，但是输入流已经关闭的异常
