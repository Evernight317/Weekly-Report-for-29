package Ex.ArrayList.Food;
/**
 * 操作菜品
 * 1.上架菜品
 * 2.浏览菜品信息
 * FoodOperator
 */

import java.util.ArrayList;
import java.util.Scanner;

public class FoodOperator {
    private ArrayList <Food> foodlist = new ArrayList<>();

    public void addFood () {
        Scanner ssc = new Scanner(System.in);
        Food f = new Food ();
        System.out.println ("请输入菜品名字");
        f.setName(ssc.next ());
        System.out.println ("请输入菜品价格");
        f.setPrice(ssc.nextDouble());
        System.out.println ("请输入菜品描述");
        f.setDes(ssc.next());

        System.out.println ("菜品信息如下");
        System.out.println ("菜名 "+f.getName()+
        " 价格 "+f.getPrice()+" 菜品描述 "+f.getDes());
        foodlist.add (f);
        System.out.println ("成功导入\n");
    }
    public void showFood () {
        if (foodlist.size () == 0) {
            System.out.println ("请先上菜喵");
            return ;
        }
        for (int i = 0,len = foodlist.size (); i < len; ++i) {
            System.out.print ("菜品名称: ");
            System.out.print (foodlist.get(i).getName()+" ");
            System.out.print ("价格: ");
            System.out.print (foodlist.get(i).getPrice()+" ");
            System.out.print ("菜品描述: ");
            System.out.println (foodlist.get(i).getDes());
        }
        System.out.println ();
    }
    public void start () {
        while (true) {
            System.out.println ("请选择功能");
            System.out.println ("1:上架菜品");
            System.out.println ("2:展示菜品");
            System.out.println ("3:退出喵");
            Scanner sc = new Scanner (System.in);
            String opt = sc.next ();
            switch (opt) {
                case "1":
                    addFood();
                    break;
                case "2":
                    showFood();
                    break;
                case "3":
                    System.out.println ("系统已退出喵");
                    sc.close();
                    return ;
                default:
                    System.out.println ("输出错误喵!请重新输入喵！\n");
                
            }
        }
    }
}
