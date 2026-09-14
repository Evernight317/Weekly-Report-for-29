/**
 * 评委打分
 */
package Ex;

import java.util.Scanner;

public class ex_3_1 {
    
    public static void main (String args[]) {
        Scanner sc = new Scanner (System.in);
        // int a[] = new int[10];
        // int n = sc.nextInt ();
        int n = 5;
        int a[] = new int[] {92,95,98,93,96};
        // for (int i = 0; i < n; ++i)
        //     a[i] = sc.nextInt();
        int maxn = 0, minn = (int) (1e9+10), sum = 0;
        for (int i = 0; i < n; ++i) {
            maxn = Math.max (maxn,a[i]);
            minn = Math.min (minn,a[i]);
            sum += a[i];
        }
        double avg = (sum-maxn-minn) / (n-2);
        System.out.println (avg);
        sc.close();
    }
}
