package Ex.ArrayList;
/**
 * ArrayList-删除特定商品
 */
import java.util.ArrayList;

public class ex_1 {
    public static void main (String args[]) {
        ArrayList<String> arr = new ArrayList<> ();
        String obj = "枸杞";
        arr.add("Java入门"); arr.add ("宁夏枸杞"); arr.add ("黑枸杞");
        arr.add ("人字拖"); arr.add ("特级枸杞"); arr.add ("枸杞子");
        System.out.println (arr);
        // for (int i = 0; i < arr.size (); ++i) {
        //     if (arr.get(i).contains(obj) == true) {
        //         arr.remove (i);
        //         --i;
        //     }
        // }
        for (int i = arr.size ()-1; i >= 0; --i) {
            if (arr.get (i).contains (obj) == true)
                arr.remove (i);
        }
        System.out.println (arr);
    }
}
