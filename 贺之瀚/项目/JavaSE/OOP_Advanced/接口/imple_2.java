import java.util.ArrayList;

public class imple_2 implements studentOperator {

    @Override
    public void printAllAverageScore(ArrayList<student> students) {
        // TODO Auto-generated method stub
        double maxn = -1,minn = 750;
        double sum = 0;
        for (int i = 0; i < students.size (); i++) {
            student s = students.get(i);
            sum += s.getScore();
            if (s.getScore() > maxn)
                maxn = s.getScore();
            if (s.getScore() < minn)
                minn = s.getScore();
        }
        sum = (sum-maxn-minn)/(students.size()-2);
        System.out.println("班级平均分为："+sum+"分");
    }

    @Override
    public void printAllInfo(ArrayList<student> students) {
        // TODO Auto-generated method stub
        int cntman = 0, cntwoman = 0;
        for (int i = 0; i < students.size (); ++i) {
            student s = students.get(i);
            if (s.getSex() == '男')
                ++cntman;
            else if (s.getSex() == '女')
                ++cntwoman;
            System.out.println("姓名："+s.getName()+" 性别："+s.getSex()+" 成绩："+s.getScore());
        }
        System.out.println("男生人数："+cntman+"人 女生人数："+cntwoman+"人");
    }
    
}
