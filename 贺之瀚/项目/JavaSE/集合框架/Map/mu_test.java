package 集合框架.Map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class mu_test {
    public static void main(String[] args) {
        List<String> lists = new ArrayList<>();
        Map<String,Integer> map = new HashMap<>();
        String[] arr = {"A","B","C","D"};
        Random r = new Random();
        for (int i = 0; i < 80; ++i) {
            int index = r.nextInt(4);
            lists.add(arr[index]);
        }
        System.out.println(lists);
        for (String s : lists) {
            if (map.containsKey(s)) {
                map.put(s,map.get(s)+1);
            }
            else {
                map.put(s, 1);
            }
        }
        Set<String> set = map.keySet();
        String tar = "";
        int tmp = 0;
        for (String s : set) {
            System.out.println(s+" "+map.get(s));
            if (tmp < map.get(s)) {
                tmp = map.get(s);
                tar = s;
            }
        }
        System.out.println(tar);
        // Set<String> set = map.keySet();
        // for (String s : set) {
        //     int num = map.get(s);
        //     System.out.println(s+"->"+num);
        // }
    }
}
