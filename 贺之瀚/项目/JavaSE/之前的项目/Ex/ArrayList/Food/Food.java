package Ex.ArrayList.Food;
/**
 * 菜品的实体类
 * Food
 */
public class Food {
    private String name;
    private double price;
    private String des; // description
    public Food () {

    }
    public Food (String name, double price, String des) {
        this.name = name;
        this.price = price;
        this.des = des;
    }
    public void setName (String name) {
        this.name = name;
    }
    public String getName () {
        return name;
    }
    public void setPrice (double price) {
        this.price = price;
    }
    public double getPrice () {
        return price;
    }
    public void setDes (String des) {
        this.des = des;
    }
    public String getDes () {
        return des;
    }
}
