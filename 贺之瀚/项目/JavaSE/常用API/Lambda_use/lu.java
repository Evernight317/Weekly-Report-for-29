package Lambda_use;

public class lu {
    public static void main(String[] args) {
        swim s0 = new swim () {

            @Override
            public void swimming() {
                // TODO Auto-generated method stub
                System.out.println("兄弟私密马赛");
            }
            
        };
        s0.swimming();
        swim s = () -> {
            System.out.println("兄弟你好香");
        };

        s.swimming();
    }
}
@FunctionalInterface
interface swim {
    void swimming ();
}
