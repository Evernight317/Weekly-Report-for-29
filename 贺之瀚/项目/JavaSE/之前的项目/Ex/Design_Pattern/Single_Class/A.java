package Ex.Design_Pattern.Single_Class;

public class A  {
    private static A a = new A();

    private A (){

    }
    public static A get_ObjectA () {
        return a;
    }
}
