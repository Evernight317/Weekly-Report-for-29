package Ex.ATM_System;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * ATM 数据处理，提供对外API
 */
public class ATM {
    private ArrayList <Account> accounts = new ArrayList<>();
    private Scanner sc = new Scanner (System.in);

    public void start () {
        System.out.println ("欢迎进入ATM系统!");
        while (true) {
            System.out.println ("请选择操作");
            System.out.println ("1.用户登录 2.用户开户 3.退出系统");
            String opt = sc.next ();
            switch (opt) {
                case "1":
                    logAccount();
                    break;
                case "2":
                    createAccount();
                    break;
                case "3":
                    System.out.println ("System close!");
                    sc.close ();
                    return ;
                default:
                    System.out.println ("error! Choose Again\n");
                    break;
            }
        }
    }
    // 此处由于仅内部调用，更适合private
    // 输入内容不检查
    private void createAccount () {
        Account acc = new Account();
        System.out.println ("请输入用户姓名");
        acc.setUserName(sc.next());
        System.out.println ("请输入用户性别");
        acc.setSex(sc.next());
        System.out.println ("请输入用户密码");
        acc.setPassWord(sc.next());
        String tmp = new String ();
        while (true) {
            System.out.println ("请再次输入密码");
            tmp = sc.next();           
            if (acc.getPassWord().equals(tmp) == false)
                System.out.println ("fail! input again!\n");
            else
                break;
        }
        System.out.println ("请输入账户每日限额");
        acc.setMoneyLimit(sc.nextDouble());
        System.out.println ("开户成功！");
        System.out.println ("姓名: "+acc.getUserName());
        System.out.println ("卡号: "+createCardId ());
        System.out.println ("性别: "+acc.getSex());
        System.out.println ("账户每日限额: "+acc.getMoneyLimit()+"\n");
        accounts.add(acc);
    }
    public String createCardId () {
        String id = new String();
        Random r = new Random();
        for (int i = 1; i <= 12; ++i) {
            id += r.nextInt (10);
        }
        return id;
    }
    public void logAccount () {
        int index = -1;
        while (true) {
            System.out.println ("请输入卡号");
            String id = sc.next();
            index = accountSearch(id);
            if (index == -1)
                System.out.println ("Unexist! Input again\n");
            else
                break;
        }
        while (true) {
            System.out.println ("请输入用户名");
            String name = sc.next();
            if (index == -1 || accounts.get (index).getUserName().equals(name) == false)
                System.out.println ("Name error! Input again\n");
            else
                break;
        } 
        while (true) {
            System.out.println ("请输入密码");
            String code = sc.next();
            if (index == -1 || accounts.get (index).getPassWord().equals(code) == false)
                System.out.println ("Password error! Input again\n");
            else
                break;
        }
        System.out.println ("用户"+accounts.get(index).getUserName()+"欢迎进入系统");
        operateAccout();
    }
    public int accountSearch (String id) {
        for (int i = 0; i < accounts.size(); ++i)
            if (accounts.get(i).getCardId().equals(id) == true)
                return i;
        return -1;
    }
    // 存取款、转账、销户、改密码
    public void operateAccout () {

    }
}
