package String_BuilderBuffer;

public class test_sbb {
    public static void main(String[] args) {
        // 报错原因：main方法是静态方法，不能调用实例方法
        // System.out.println(printArrayData (new int[] {1,29,3,42}));
        System.out.println(printArrayData (new int[] {1,29,3,42}));
    }
    public static String printArrayData (int[] arr) {
        // 判空
        if (arr == null)
            return null;
        StringBuilder sb = new StringBuilder();
        sb.append ("[");
        for (int i = 0; i < arr.length; i++) {
            if (i == arr.length-1)
                sb.append (i).append("]");
            else
                sb.append (i).append (", ");
        }
        return sb.toString();
    }
}
