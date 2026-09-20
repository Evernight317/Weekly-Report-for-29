package aaa;

public class test_2 {
    public static void main (String args[]) {
        int arr[] = {1,2,3};
        for (int i = 0; i < 3; ++i)
            System.out.print (arr[i]+" ");
        System.out.println ();
        int arr1[] = new int[10];
        for (int i = 0; i < 10; ++i)
            arr1[i] = i;
        for (int i = 0; i < 10; ++i)
            System.out.print (arr1[i]+" ");
        System.out.println ();
        int maxn = 0;
        for (int i = 0; i < 10; ++i)
            maxn = Math.max (maxn,arr1[i]);
        System.out.println (maxn);
    }
}
