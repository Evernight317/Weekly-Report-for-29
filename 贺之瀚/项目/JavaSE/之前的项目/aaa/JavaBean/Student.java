package aaa.JavaBean;
/**
 * 实体类的实际应用模拟
 */

public class Student {
    private String name;
    private double score;

    public Student () {

    }
    public void setname (String name) {
        this.name = name;
    }
    public String getname () {
        return name;
    }
    public void setscore (double score) {
        this.score = score;
    }
    public double getscore () {
        return score;
    }
}
