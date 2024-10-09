



import java.awt.*;
import java.text.*;
import java.util.*;
import java.io.*;

public class DFSSerialization142 {

    public static void main(String[] args)
    {
        try {

            DecimalFormatSymbols dfs= new DecimalFormatSymbols();
            System.out.println("Default currency symbol in the default locale : "  + dfs.getCurrencySymbol());
            dfs.setCurrencySymbol("*SpecialCurrencySymbol*");
            System.out.println("The special currency symbol is set : "  + dfs.getCurrencySymbol());
            FileOutputStream ostream = new FileOutputStream("DecimalFormatSymbols.142");
            ObjectOutputStream p = new ObjectOutputStream(ostream);
            p.writeObject(dfs);
            ostream.close();
            System.out.println("DecimalFormatSymbols saved ok.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
