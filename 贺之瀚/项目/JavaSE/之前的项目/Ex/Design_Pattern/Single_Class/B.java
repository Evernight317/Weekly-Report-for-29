package Ex.Design_Pattern.Single_Class;

public class B {
    public static B b;
    private B () {

    }
    public static B get_ObjectB () {
        if (b == null) {
            b = new B();
        }
        return b;
    }
}
