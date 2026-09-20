package 集合框架.Collections_Use_斗地主;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 房间
 * Room
 */
public class Room {
    private List<Card> allCards = new ArrayList<>();

    public Room () {
        String[] numbers = {"3","4","5","6","7","8","9","10","J","Q","K","A","2"};
        String[] colors = {"梅花","方块","红桃","黑桃"};
        int size = 0;
        for (String number : numbers) {
            ++size;
            for (String color : colors) {
                Card c = new Card(number,color,size);
                allCards.add(c);
            }
        }
        allCards.add(new Card("","jOKER",++size));
        allCards.add(new Card("","JOKER",++size));
    }

    // 要重写toString方法，否则输出的是对象地址
    public void getAllCards() {
        // System.out.println(allCards);
        for (Card c : allCards) {
            System.out.print(c.toString()+" ");
        }
    }

    public void start() {
        // 1.洗牌
        Collections.shuffle(allCards);
        System.out.println("洗牌完成");
        // getAllCards();

        // 2.发牌
        // 这里用List来模拟玩家对象
        // 由于是循环发牌，可以用求余写，记得留三张底牌
        List<Card> lbw = new ArrayList<>();
        List<Card> lbwnb = new ArrayList<>();
        List<Card> lbwgc = new ArrayList<>(); 
        for (int i = 0; i < allCards.size()-3; ++i) {
            if (i % 3 == 0)
                lbw.add(allCards.get(i));
            else if (i % 3 == 1)
                lbwnb.add(allCards.get(i));
            else if (i % 3 == 2)
                lbwgc.add(allCards.get(i));
        }
        // System.out.print("\nlbw: ");
        // for (Card c : lbw)
        //     System.out.print (c+" ");
        // System.out.print("\nlbwnb: ");
        // for (Card c : lbwnb)
        //     System.out.print (c+" ");
        // System.out.print("\nlbwgc: ");
        // for (Card c : lbwgc)
        //     System.out.print (c+" ");
        
        // 排序
        sortCard (lbw);
        sortCard(lbwnb);
        sortCard(lbwgc);
        System.out.println (lbw);
        System.out.println (lbwnb);
        System.out.println (lbwgc);

        // 把底牌截取出来成为一个集合，可以方便给地主发牌的操作
        List<Card> restCard = allCards.subList(allCards.size()-3, allCards.size());
        System.out.println("地主牌为："+restCard);

        // 假设lbw是地主
        lbw.addAll(restCard);
        sortCard(lbw);
        System.out.println ("地主是lbw，牌如下\n"+lbw);
        // System.out.println (lbw);
    }
    private void sortCard (List<Card> cards) {
        Collections.sort (cards, (c1,c2) -> c1.getSize()-c2.getSize());
    }
}
