package BigDecimal_use;

import java.math.BigDecimal;

public class BDu {
    public static void main(String[] args) {
        double a = 0.1, b= 0.2;
        double c = a+b;
        System.out.println(c);
        System.out.println();
        BigDecimal a1 = BigDecimal.valueOf (a);
        BigDecimal b1 = BigDecimal.valueOf (b);
        BigDecimal c1 = a1.add (b1);
        System.out.println(c1);

    }
}
