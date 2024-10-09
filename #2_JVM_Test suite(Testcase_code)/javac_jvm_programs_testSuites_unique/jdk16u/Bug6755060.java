



import java.text.Collator;
import java.util.Arrays;
import java.util.Locale;

public class Bug6755060 {

  
  public static void main (String[] args) {

    Locale reservedLocale = Locale.getDefault();

    try{

        int errors=0;

        Locale loc = new Locale ("th", "TH");   

        Locale.setDefault (loc);
        Collator col = Collator.getInstance ();

        

        String[] data = {"\u0e01", "\u0e01\u0e2f", "\u0e01\u0e46", "\u0e01\u0e4f", "\u0e01\u0e5a", "\u0e01\u0e5b", "\u0e01\u0e4e", "\u0e01\u0e4c", "\u0e01\u0e48", "\u0e01\u0e01", "\u0e01\u0e4b\u0e01", "\u0e01\u0e4d", "\u0e01\u0e30", "\u0e01\u0e31\u0e01", "\u0e01\u0e32", "\u0e01\u0e33", "\u0e01\u0e34", "\u0e01\u0e35", "\u0e01\u0e36", "\u0e01\u0e37", "\u0e01\u0e38", "\u0e01\u0e39", "\u0e40\u0e01", "\u0e40\u0e01\u0e48", "\u0e40\u0e01\u0e49", "\u0e40\u0e01\u0e4b", "\u0e41\u0e01", "\u0e42\u0e01", "\u0e43\u0e01", "\u0e44\u0e01", "\u0e01\u0e3a", "\u0e24\u0e32", "\u0e24\u0e45", "\u0e40\u0e25", "\u0e44\u0e26"};

        String[] sortedData = {"\u0e01", "\u0e01\u0e2f", "\u0e01\u0e46", "\u0e01\u0e4f", "\u0e01\u0e5a", "\u0e01\u0e5b", "\u0e01\u0e4e", "\u0e01\u0e4c", "\u0e01\u0e48", "\u0e01\u0e01", "\u0e01\u0e4b\u0e01", "\u0e01\u0e4d", "\u0e01\u0e30", "\u0e01\u0e31\u0e01", "\u0e01\u0e32", "\u0e01\u0e33", "\u0e01\u0e34", "\u0e01\u0e35", "\u0e01\u0e36", "\u0e01\u0e37", "\u0e01\u0e38", "\u0e01\u0e39", "\u0e40\u0e01", "\u0e40\u0e01\u0e48", "\u0e40\u0e01\u0e49", "\u0e40\u0e01\u0e4b", "\u0e41\u0e01", "\u0e42\u0e01", "\u0e43\u0e01", "\u0e44\u0e01", "\u0e01\u0e3a", "\u0e24\u0e32", "\u0e24\u0e45", "\u0e40\u0e25", "\u0e44\u0e26"};

        Arrays.sort (data, col);

        System.out.println ("Using " + loc.getDisplayName());
        for (int i = 0;  i < data.length;  i++) {
            System.out.println(data[i] + "  :  " + sortedData[i]);
            if (sortedData[i].compareTo(data[i]) != 0) {
                errors++;
            }
        }

        if (errors > 0){
            StringBuffer expected = new StringBuffer(), actual = new StringBuffer();
            expected.append(sortedData[0]);
            actual.append(data[0]);

                for (int i=1; i<data.length; i++) {
                    expected.append(",");
                    expected.append(sortedData[i]);

                    actual.append(",");
                    actual.append(data[i]);
                }

            String errmsg = "Error is found in collation testing in Thai\n" + "exepected order is: " + expected.toString() + "\n" + "actual order is: " + actual.toString() + "\n";

            throw new RuntimeException(errmsg);
        }
    }finally{
        
        Locale.setDefault(reservedLocale);
    }

  }

}
