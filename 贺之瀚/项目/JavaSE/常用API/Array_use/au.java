package Array_use;
/**
 * Arrays.setAll方法的使用
 */
import java.util.Arrays;
import java.util.function.IntToDoubleFunction;


public class au {
    public static void main(String[] args) {
        // setall_use ();
        // sort_use();
        diy_sort1();
        System.out.println();
        diy_sort2();
    }
    public static void setall_use () {
        double[] arr = {80,100,120};
        System.out.println(Arrays.toString(arr));
        Arrays.setAll(arr, new IntToDoubleFunction() {
            @Override
            // 此处value对应的是数组的索引，依次从0取到末尾
            // 追求精度使用BigDecimal
            public double applyAsDouble(int value) {
                // TODO Auto-generated method stub
                return arr[value]*0.8;
            }
            
        });
        System.out.println(Arrays.toString(arr));
    }
    public static void sort_use () {
        int[] arr = {100,2,288,3,6};
        Arrays.sort (arr);
        System.out.println(Arrays.toString(arr));
    }
    public static void diy_sort1 () {

    }
    public static void diy_sort2 () {

    }
}
