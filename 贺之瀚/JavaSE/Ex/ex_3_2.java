/**
 * 数字加密
 */

package Ex;

import java.util.Scanner;

public class ex_3_2 {
    public static void main (String args[]) {
        Scanner sc = new Scanner (System.in);
        int a = sc.nextInt (), b = 0;
        // int a = 1983, b = 0;
        while (a != 0) {
            int tmp = a % 10;
            a /= 10;
            tmp = (tmp+5) % 10;
            b = b*10 + tmp;
        }
        System.out.println (b);
        sc.close ();
    }
}
