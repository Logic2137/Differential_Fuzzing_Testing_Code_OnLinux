



import java.awt.*;
import java.text.*;
import java.util.*;
import java.io.*;

public class SerializationSaveTest {

    public static void main(String[] args)
    {
        try {
            CheckDecimalFormat it = new CheckDecimalFormat();
            System.out.println(it.Update());
            FileOutputStream ostream = new FileOutputStream("DecimalFormat.114");
            ObjectOutputStream p = new ObjectOutputStream(ostream);
            p.writeObject(it);
            ostream.close();
            System.out.println("DecimalFormat saved ok.");
            CheckDecimalFormatSymbols it2 = new CheckDecimalFormatSymbols();
            System.out.println("getDigit : "  + it2.Update());
            FileOutputStream ostream2 = new FileOutputStream("DecimalFormatSymbols.114");
            ObjectOutputStream p2 = new ObjectOutputStream(ostream2);
            p2.writeObject(it2);
            ostream2.close();
            System.out.println("DecimalFormatSymbols saved ok.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

@SuppressWarnings("serial")
class CheckDecimalFormat implements Serializable
{
    DecimalFormat _decFormat = (DecimalFormat)NumberFormat.getInstance();

    public String Update()
    {
        Random r = new Random();
        return _decFormat.format(r.nextDouble());
    }
}

@SuppressWarnings("serial")
class CheckDecimalFormatSymbols implements Serializable
{
    DecimalFormatSymbols _decFormatSymbols = new DecimalFormatSymbols();

    public char Update()
    {
        return  _decFormatSymbols.getDigit();
    }
}
