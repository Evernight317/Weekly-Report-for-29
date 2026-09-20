package 集合框架.集合嵌套;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * 集合嵌套
 * Embedded
 */
public class Embedded {
    public static void main(String[] args) {
        Map<String,List<String> > provinces = new HashMap<>();
        List<String> p1 = new ArrayList<>();
        Collections.addAll(p1,"南京市", "扬州市", "苏州市"," 无锡市","常州市");
        provinces.put("江苏省", p1);
        List<String> p2 = new ArrayList<>();
        Collections.addAll(p2,"武汉市", "孝感市", "十堰市", "宜昌市", "鄂州市");
        provinces.put("湖北省", p2);
        List<String> p3 = new ArrayList<>();
        Collections.addAll(p3,"石家庄市", "唐山市", "邢台市", "保定市", "张家口市");
        provinces.put("河北省", p3);
        List<String> pro = provinces.get("湖北省");
        System.out.println(pro);
        System.out.println();
        Set<String> pros = provinces.keySet();
        for (String s : pros) {
            List<String> lists = provinces.get(s);
            System.out.println(lists);
        }
        provinces.forEach((k,v) -> System.out.println(k+" -> "+v));
    }
}
