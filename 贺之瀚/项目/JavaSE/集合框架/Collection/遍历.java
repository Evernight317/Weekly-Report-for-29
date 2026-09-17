package 集合框架.Collection;
/**
 * 本质是增强for循环
 */
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

public class 遍历 {
    public static void main(String[] args) {
        Collection<String> c = new ArrayList<>();
        c.add("赵敏");
        c.add("小昭");
        c.add("股素素");
        c.add("周芷若");

        // 完整写法，匿名内部类
        c.forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                // TODO Auto-generated method stub
                System.out.println(s);
            }
        });

        // Lambda简化
        c.forEach(s -> System.out.println(s));
        // 实例方法引用
        // out是一个对象，println则是实例方法
        c.forEach(System.out::println);
    }
}
