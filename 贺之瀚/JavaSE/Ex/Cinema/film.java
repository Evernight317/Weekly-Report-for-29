package Ex.Cinema;

public class film {
    private int id;
    private String name;
    private double price;
    private double score;
    private String director;
    private String actor;
    private String info;

    // private int id;
    public film () {

    }
    public void Setid (int id) {
        this.id = id;
    }
    public int Getid () {
        return id;
    }
    public void Setname (String name) {
        this.name = name;
    }
    public String Getname () {
        return name;
    }
    public void Setprice (double price) {
        this.price = price;
    }
    public double Getprice () {
        return price;
    }
    public void Setscore (double score) {
        this.score = score;
    }
    public double Getscore () {
        return score;
    }
    public void Setdirector (String director) {
        this.director = director;
    }
    public String Getdirector () {
        return director;
    }
    public void Setactor (String actor) {
        this.actor = actor;
    }
    public String Getactor () {
        return actor;
    }
    public void Setinfo (String info) {
        this.info = info;
    }
    public String Getinfo () {
        return info;
    }    
}
