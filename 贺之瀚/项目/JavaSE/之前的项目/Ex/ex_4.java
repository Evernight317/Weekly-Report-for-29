/**
 * 抢红包
 */
package Ex;

import java.util.Random;

public class ex_4 {
    
    public static void main (String args[]) {
        int p[] = new int[] {9,666,188,520,99999};
        int vis[] = new int [] {0,0,0,0,0};
        for (int i = 0; i < 5; ++i) {
            int res = p[redbag(p, vis)];
            // int res = redbag(p, vis);
            System.out.println (res);
        }
    }
    public static int redbag (int p[], int vis[]) {
        Random r = new Random();
        int tmp = r.nextInt(5);
        while (vis[tmp] != 0) {
            tmp = r.nextInt(5);
        }
        vis[tmp] = 1;
        return tmp;
    }
}
