package aaa.JavaBean;

public class test {
    public static void main (String args[]) {
        Student s1 = new Student (), s2 = new Student ();
        s1.setname("势果园");
        s1.setscore(61);
        s2.setname("势邓艾");
        s2.setscore(59);
        StudentOperator so1 = new StudentOperator (s1);
        so1.printpass();
        StudentOperator so2 = new StudentOperator (s2);
        so2.printpass();
    }
}
