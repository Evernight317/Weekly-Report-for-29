package Array_use;

public class student implements Comparable<student> {
    String name;
    double h;
    int age;
    @Override
    public int compareTo(student o) {
        // TODO Auto-generated method stub
        return this.age-o.age;
        // 降序
        // return o.age-this.age;
    }


}
