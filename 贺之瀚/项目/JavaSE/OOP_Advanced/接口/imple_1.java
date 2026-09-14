import java.util.ArrayList;

public class imple_1 implements studentOperator {

    @Override
    public void printAllAverageScore(ArrayList<student> students) {
        // TODO Auto-generated method stub
        double sum = 0;
        for (int i = 0; i < students.size (); i++) {
            student s = students.get(i);
            sum += s.getScore();
        }
        sum /= students.size ();
        System.out.println("班级平均分为："+sum+"分");        
    }

    @Override
    public void printAllInfo(ArrayList<student> students) {
        // TODO Auto-generated method stub
        for (int i = 0; i < students.size (); ++i) {
            student s = students.get(i);
            System.out.println("姓名："+s.getName()+" 性别："+s.getSex()+" 成绩："+s.getScore());
        }
    }
    
}
