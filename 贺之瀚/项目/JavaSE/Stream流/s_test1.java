package Stream流;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class s_test1 {
    public static void main(String[] args) {
        List<String> names  = new ArrayList<>();
        List<String> lists1 = new ArrayList<>();
        Collections.addAll(names,"张无忌","周芷若","赵敏","张强");
        for (String name : names) {
            if(name.startsWith("张") && name.length() == 3) {
                lists1.add(name);
            }
        }
        // List<String> lists = names.stream().filter(s -> s.startsWith("张")
        //  && s.length() == 3).collect(Collectors.toList());
        List<String> lists2 = names.stream().filter(s -> s.startsWith("张"))
        .filter(s -> s.length()==3).collect(Collectors.toList());
        System.out.println(lists1);
        System.out.println(lists2);
    }
}
