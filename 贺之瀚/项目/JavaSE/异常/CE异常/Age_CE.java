package 异常.CE异常;


public class Age_CE {
    public static void main(String[] args) {
        int tmp = 1200;
        // 在此处会提示异常，也可以在main方法上加throws继续向上抛
        // saveAge2(tmp);

        // 也可以捕获异常，不会直接红线提醒，但是仍无法通过编译
        try {
            saveAge2(tmp);
            System.out.println("要去了要去了");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            System.out.println("宝宝你好大好深");
        }

    }
    // 编译时异常，如果没有throws的话就是一写出来就在throw处红线报错
    // 处理方法是加上关键字throws
    // throws关键字用在方法上，用于抛出方法内部的异常
    // 加上后会被转到上一级调用者，即main方法对应处红线提醒
    public static void saveAge2 (int age) throws AgeIllegalCompileException{
        if (age > 0 && age < 150) {
            System.out.println("医术高超");
        } else {
            throw new AgeIllegalCompileException("年龄错误，你的年龄是" + age + "岁！年龄应该0-150岁之间！");
        }
    }
}
