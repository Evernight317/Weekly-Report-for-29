package aaa.JavaBean;

public class StudentOperator {
    // 需要接收Student对象
    private Student s;
    public StudentOperator () {

    }
    public StudentOperator (Student s) {
        this.s = s;
    }
    public void printpass () {
        if (s.getscore() >= 60) {
            System.out.println (s.getname ()+" "+s.getscore()+"分 pass");
        }
        else {
            System.out.println (s.getname ()+" "+s.getscore()+"分 fail");
        }
    }
}
