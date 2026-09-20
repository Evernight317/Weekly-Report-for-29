public class Zi extends Constuctor_Fu {
    private String skill;

    public Zi (){

    }
    public Zi(String name,int age, String skill) {
        // 这种方法在变量多时码量大，繁琐
        // 而通过调用父类构造器的方式可以实现对所有成员的快速初始化
        // setName(name);
        // setAge(age);

        // 继承将一个类的所有成员变量拆分到多个类中
        // 相应的，调用构造器时就必须先从父类出发，逐步调用多个构造器，将所有成员变量初始化
        super (name,age);
        this.skill = skill;
    }
    
}
