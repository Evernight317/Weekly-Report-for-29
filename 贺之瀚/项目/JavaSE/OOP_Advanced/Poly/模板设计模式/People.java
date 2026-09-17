import java.nio.channels.Pipe.SourceChannel;

public abstract class People {
    
    public void write () {
        System.out.println("111");
        System.out.println(writeMain());
        System.out.println("333");
    }
    public abstract String writeMain ();
}
