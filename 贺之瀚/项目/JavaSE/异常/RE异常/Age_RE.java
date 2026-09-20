package 异常.RE异常;

/**
 * 自定义RE
 */
import java.util.Scanner;

public class Age_RE {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // int tmp = sc.nextInt();
        int tmp = 1200;
        try {
            saveAge(tmp);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            System.out.println("妙手回春");
        }
        
        sc.close();
    }

    public static void saveAge(int age) {
        if (age > 0 && age < 150) {
            System.out.println("医术高超");
        } else {
            // 用异常对象封装问题
            // 并且用throw将异常抛出至入口，并返回给调用者
            throw new AgeIllegalRuntimeException("年龄错误！你的年龄是" + age + "岁");
        }
    }
}
