package 正则表达式;

import java.util.Scanner;

/**
 * 校验QQ号码是否正确
 * 要求均为数字，长度是[6,10]之间，不能以0开头
 * Regex_use
 */
public class Regex_use {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String tmp = sc.next();
        // tmp = null;
        if (check (tmp))
            System.out.println("兄弟你好香");
        else
            System.out.println("兄弟私密马赛");
        sc.close();
    }
    public static boolean check (String qq) {
        return qq != null && qq.matches ("[1-9]\\d{5,9}");
    }
}
