package Ex.String;
/**
 * 验证码生成
 */
import java.util.Random;
import java.util.Scanner;

public class Verification_Code {
    public static void main (String args[]) {
        Scanner sc = new Scanner (System.in);
        // System.out.println ("请输入验证码位数");
        // int n = sc.nextInt ();
        int n = 4;
        String code = generateCode (n);
        System.out.println ("验证码如下："+code);
        sc.close ();
    }
    public static String generateCode (int digit) {
        String code = new String ();
        String ch = "abcdefghijklmnopqrstuVwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int len = ch.length ();
        Random r = new Random ();
        for (int i = 0; i < digit; ++i) {
            int tmp = r.nextInt(len);
            code += ch.charAt (tmp); // 此处是+的连接作用

        }
        return code;
    }
}
