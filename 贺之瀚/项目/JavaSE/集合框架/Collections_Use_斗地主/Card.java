package 集合框架.Collections_Use_斗地主;
/**
 * 得得得得得得得得得得得得得
 * 卡牌
 */
public class Card {
    private String number;
    private String color;
    private int size;// 比大小用的
    
    public Card() {
    }
    public Card(String number, String color, int size) {
        this.number = number;
        this.color = color;
        this.size = size;
    }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }
    @Override
    public String toString() {
        if (number == null)
            return color;
        return color+number;
        // if (number == null)
        //     return color+" --> "+size;
        // return color+" "+number+" --> "+size;
    }
    
    
}
