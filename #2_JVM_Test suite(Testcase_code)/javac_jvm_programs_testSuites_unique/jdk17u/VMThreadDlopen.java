import java.io.File;

public class VMThreadDlopen {

    public static void main(String[] args) throws Exception {
        File file = new File("libbroken.so");
        file.createNewFile();
        try {
            System.loadLibrary("broken");
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
        }
    }
}
