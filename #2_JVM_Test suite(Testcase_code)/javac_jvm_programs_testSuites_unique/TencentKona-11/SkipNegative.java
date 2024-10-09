





import java.io.*;

public class SkipNegative {
    public static void main(String argv[]) throws Exception {
        File f = new File(System.getProperty("test.src", "."),
                          "SkipInput.txt");
        FileReader fr = new FileReader(f);
        long nchars = -1L;
        try {
            long actual = fr.skip(nchars);
        } catch(IllegalArgumentException e){
            
            return;
        } finally {
            fr.close();
        }
        throw new Exception("Skip should not accept negative values");
    }
}
