import java.awt.*;
import java.text.*;
import java.util.*;
import java.io.*;

public class DFSDeserialization142 {

    public static void main(String[] args) {
        try {
            File file = new File("DecimalFormatSymbols.current");
            FileInputStream istream = new FileInputStream(file);
            ObjectInputStream p = new ObjectInputStream(istream);
            DecimalFormatSymbols dfs = (DecimalFormatSymbols) p.readObject();
            if (dfs.getCurrencySymbol().equals("*SpecialCurrencySymbol*")) {
                System.out.println("Serialization/Deserialization Test Passed.");
            } else {
                throw new Exception("Serialization/Deserialization Test Failed:" + dfs.getCurrencySymbol());
            }
            istream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
