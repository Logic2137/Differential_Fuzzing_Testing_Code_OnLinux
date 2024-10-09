



import java.util.*;
import java.text.*;
import java.io.*;

public class bug6271375 {

    public static void main(String[] args) throws Exception {
        DateFormatSymbols dfsSrc = DateFormatSymbols.getInstance();

        try (FileOutputStream fos = new FileOutputStream("dfs.ser");
             ObjectOutputStream oStream = new ObjectOutputStream(fos)) {
            oStream.writeObject(dfsSrc);
        } catch (Exception e) {
            throw new RuntimeException("An exception is thrown.", e);
        }

        try (FileInputStream fis = new FileInputStream("dfs.ser");
             ObjectInputStream iStream = new ObjectInputStream(fis)) {
            DateFormatSymbols dfsDest = (DateFormatSymbols)iStream.readObject();

            String[][] zoneStringsSrc = dfsSrc.getZoneStrings();
            String[][] zoneStringsDest = dfsDest.getZoneStrings();

            if (!Arrays.deepEquals(zoneStringsSrc, zoneStringsDest)) {
                throw new RuntimeException("src and dest zone strings are not equal");
            }
        } catch (Exception e) {
            throw new RuntimeException("An exception is thrown.", e);
        }
    }
}
