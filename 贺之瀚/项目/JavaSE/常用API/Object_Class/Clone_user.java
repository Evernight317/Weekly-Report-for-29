package Object_Class;
public class Clone_user implements Cloneable{
    private String name;
    private int age;

    public Clone_user () {
        name = "银狼Lv.999";
        age = 18;
    }

    public Clone_user(String name, int age) {
        this.name = name;
        this.age = age;
    }
    public void tmp () {
        System.out.println(name+age);
    }
    @Override
    protected Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }
}
