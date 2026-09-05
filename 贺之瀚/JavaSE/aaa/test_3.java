package aaa;

// import java.util.Scanner;

public class test_3 {
    // Scanner sc = new Scanner (System.in);
    public static void main (String args[]) {
        // int c = sum (1,2);
        // System.out.println (c);

        // donkey();

        System.out.println (sum (5));
        judge (5); judge (6);

    }
    public static int sum(int a,int b) {
        return a+b;
    }
    public static void donkey () {
        for (int i = 0; i < 3; ++i)
            System.out.println ("danil");
        return ;
    }
    public static int sum (int n) {
        int res = 0;
        for (int i = 1; i <= n; ++i)
            res += i;
        return res;
    }
    public static void judge (int num) {
        if (num % 2 == 0)
            System.out.println ("even");
        else
            System.out.println ("odd");
    }
}
