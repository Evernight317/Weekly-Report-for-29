/**
 * 买飞机票
 */
package Ex;

public class ex_1 {
    public static void main (String args[]) {
        double val = flight(1000, 8, "经济舱");
        System.out.println (val);
    }
    public static double flight (int v,int month,String type) {
        double val = 0;
        if (month >= 5 || month <= 10)
            switch (type) {
                case "头等舱" :
                    val = 0.9 * v;
                    break;
                case "经济舱" :
                    val = 0.85 * v;
                    break;
            }
        else
            switch (type) {
                case "头等舱" :
                    val = 0.7 * v;
                    break;
                case "经济舱" :
                    val = 0.65 * v;
                    break;
            }
        return val;
    }
}
