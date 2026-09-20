package aaa;

import java.util.Random;

public class test_1 {
    public static void main (String args[]) {
        Random r = new Random();
        int num = r.nextInt(10);
        System.out.println (num);
        // for (int i = 1; i <= 9; ++i) {
        //     for (int j = 1; j <= i; ++j) {
        //         System.out.print (i+" * "+j+" = ");
        //         System.out.printf ("%2d%s",i*j, "  ");
        //     }   
        //     System.out.println (); 
        // }
    }
}
