package Ex.String;
/**
 * 模拟用户登录系统，此处用户名和密码事先确定
 */
import java.util.Scanner;

public class AccountLogin {
    public static void main (String args[]) {        
        Scanner sc = new Scanner(System.in);
        String name = new String (), password = new String ();
        int cnt;
        for (cnt = 1; cnt <= 3; ++cnt) {
            System.out.println ("请输入用户名和密码");
            name = sc.next ();
            password = sc.next ();
            if (loginjudge (name, password) == true) {
                System.out.println ("登陆成功^_^");
                break;
            }
            else if (cnt != 3)
                System.out.println ("用户名或密码错误，请重新输入!\n");
            else
                System.out.println ("错误次数过多，系统已锁定！");
        }           
        sc.close ();
    }

    public static boolean loginjudge (String name, String password) {
        String rn = "势胃炎火龙果", rpw = "123456";
        if (rn.equals(name) == true && rpw.equals (password) == true)
            return true;
        else
            return false;
    }
}
