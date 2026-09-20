package Date_before_jdk8;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 商品秒杀活动，开始时间2026年9月9日 0:0:0，结束时间2026年9月9日 0:10:
 * 小贾下单付款时间为2026年9月9日 0:01:18，小皮为2026年9月9日 0:10:57
 * 判断是否秒杀成功
 * 
 * 转换成毫秒值比较
 * d
 */
public class d  {
    public static void main(String[] args) throws ParseException {
        String start ="2026年9月9日 0:0:0";
        String end="2026年9月9日 0:10:0";
        String jia="2026年9月9日 0:01:18";
        String pi="2026年9月9日 0:10:57";
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY年MM月dd日 HH:mm:ss");
        Date startdt = sdf.parse (start);
        Date enddt = sdf.parse (end);
        Date jdt = sdf.parse (jia);
        Date pdt = sdf.parse (pi);

        long sT = startdt.getTime();
        long eT = enddt.getTime();
        long jT = jdt.getTime();
        long pT = pdt.getTime();
        System.out.println("小贾"+check(sT, eT, jT));
        System.out.println("小皮"+check(sT, eT, pT));
    }
    public static String check (long s,long e,long t) {
        if (t >= s && t <= e)
            return "秒杀成功";
        else
            return "秒杀失败";
    }

}
