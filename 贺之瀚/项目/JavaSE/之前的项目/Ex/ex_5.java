/**
 * 判断素数
 */
package Ex;

import java.util.Random;

public class ex_5 {
    public static void main (String args []) {
        Random r = new Random();
        int num = r.nextInt(1001);
        // int num = 97;
        System.out.print (num+" ");
        if (judge(num) == true)
            System.out.println ("Yes");
        else
            System.out.println ("No");
    }
    public static boolean judge (int num) {
        if (num % 2 == 0)
            return false;
        for (int i = 3; i*i <= num; i += 2) {
            if (num % i == 0)
                return false;
        }
        return true;
    }
}
