/**
 * 数组拷贝
 */


// 此处Copy方法中tmp数组的销毁时机注意
package Ex;

import java.util.Scanner;

public class ex_3_3 {
    public static void main (String args[]) {
        Scanner sc = new Scanner (System.in);
        int a[] = {11,22,33};
        int b[] = Copy (a);
        for (int i = 0; i < b.length; ++i)
            System.out.print (b[i]+" ");
        System.out.println ();
        sc.close ();
    }
    public static int [] Copy (int arr[]) {
        int tmp [] = new int [arr.length];
        for (int i = 0; i < arr.length; ++i)
                tmp[i] = arr[i];
        return tmp;
    }
}
