



import com.sun.tools.javap.Main;
import java.io.*;

public class ExtPath {
    public static void main(String[] args) {
        PrintWriter pw = new PrintWriter(System.out, true);
        Main.run(args, pw);
    }
}
