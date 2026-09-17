package Array_use;

import java.util.Arrays;
import java.util.Comparator;

/**
 * 自定义排序
 * 方法一
 * 让该对象的类实现Comparable<T>(比较规则)接口，
 * 然后重写compareTo方法，自己来制定比较规则；
 * 方法二
 * 使用重载的sort方法，
 * 创建comparator<T>比较器接口的匿名内部类对象，
 * 然后自己制定比较规则；
 */
public class diysort {
    public static void main(String[] args) {
        student[] s = new student[4];
        Arrays.sort(s,new Comparator<student>() {
            @Override
            public int compare(student o1, student o2) {
                // TODO Auto-generated method stub
                return o1.age-o2.age;
                // 降序
                // return o2.age-o1.age;

                // 对double比较
                // return Double.compare(o1.h, o2.h);
                // 降序
                // return Double.compare(o2.h,o1.h);
            }
            
        });
    }
}
