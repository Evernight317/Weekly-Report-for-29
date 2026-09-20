import java.util.ArrayList;

public class classManager {
    private ArrayList <student> students = new ArrayList<>();
    private studentOperator so1 = new imple_1();
    private studentOperator so2 = new imple_2();

    public classManager (){
        students.add(new student("迪丽热巴",'女',99));
        students.add(new student("古力娜扎",'女',100));
        students.add(new student("马尔扎哈",'男',80));
        students.add(new student("卡尔扎巴",'男',60));
    }
    public void printInfo () {
        so1.printAllInfo(students);
        so1.printAllAverageScore(students);
        // so2.printAllInfo(students);
        // so2.printAllAverageScore(students);
    }
    public void printAverageScore () {
        so2.getClass();
    }
}
