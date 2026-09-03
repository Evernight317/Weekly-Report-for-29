/**
 * 验证码生成
 */
package Ex;
import java.util.Random;
import java.util.Scanner;
public class ex_2 {
    public static void main (String args[]) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        String res = code (n);
        System.out.println (res);
        sc.close();
    }
    public static String code (int n) {
        String tmp = "";
        Random r = new Random();
        for (int i = 1; i <= n; ++i) {
            int opt = r.nextInt(3);
            switch (opt) {
                case 0 :
                    tmp += r.nextInt(10);
                    break;
                case 1 :
                    tmp += (char) (65+r.nextInt(26));
                    break;
                case 2:
                    tmp += (char) (97+r.nextInt(26));
                    break;
            }
        }
        return tmp;
    }
}
