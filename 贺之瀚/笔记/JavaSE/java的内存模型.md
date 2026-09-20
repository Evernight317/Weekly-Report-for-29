# 内存分配

---



`java` 的内存分为方法区、栈、堆（栈帧？）

| 方法区             | 栈             | 堆 |
| ------------------ | -------------- | -- |
| 用于加载.class文件 | 函数在栈上执行 |    |
|                    |                |    |

## 不同数据类型的内存分配方式

java中，除了8种基本数据类型之外，其余均被视为对象，包括 **数组、字符串** 。因此，这些实际都在堆上存储，只在栈上保留引用

| 基本类型 | 引用类型                                                     |
| -------- | ------------------------------------------------------------ |
| 栈上存储 | 数据在堆上存储，栈上保留一个引用变量，存储的是指向堆上的地址 |

## 变量作用域

似乎与 `C++` 类似


## *引用类型的使用方式

`java` 的思想是 **万物皆对象** ，因此对于引用类型：  

申请 `new` 一个对象； 通过调用对象的成员函数实现相关功能；  

与 `C++` 相比，由于没有运算符重载这一功能，因此显得较为繁琐

## 内存的释放时机

以这个数组拷贝代码为例

```Java
package Ex;
import java.util.Scanner;
public class ex_3_3 {
    public static void main (String args[]) {
        Scanner sc = new Scanner (System.in);
        int a[] = {11,22,33};
        int b[] = Copy (a);
        for (int i = 0; i < b.length; ++i)
            System.out.print (b[i]+" ");
        System.out.println ();
        sc.close ();
    }
    public static int [] Copy (int arr[]) {
        int tmp [] = new int [arr.length];
        for (int i = 0; i < arr.length; ++i)
                tmp[i] = arr[i];
        return tmp;
    }
}
```

`java` 中，栈上变量会在方法结束时被立刻销毁，而堆上则由GC决定，当对象不可达时才会被销毁。 `Copy` 函数中， `tmp` 在栈上的引用变量会被立刻销毁，而堆上实际存储的数据由于被赋给了 `main` 中的 `b` ，仍然可达，所以不会被立刻销毁
