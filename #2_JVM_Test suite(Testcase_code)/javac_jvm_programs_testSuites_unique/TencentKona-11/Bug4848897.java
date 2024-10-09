



import java.text.*;
import java.util.*;

public class Bug4848897 {

  
  public static void main (String[] args) {
      Locale reservedLocale = Locale.getDefault();
      try {
          int errors=0;
          Locale loc = new Locale ("no", "NO");   

          Locale.setDefault (loc);
          Collator col = Collator.getInstance ();

          String[] data = {"wird",
                           "vird",
                           "verd",
                           "werd",
                           "vard",
                           "ward"};

          String[] sortedData = {"vard",
                                 "verd",
                                 "vird",
                                 "ward",
                                 "werd",
                                 "wird"};

          Arrays.sort (data, col);

          System.out.println ("Using " + loc.getDisplayName());
          for (int i = 0;  i < data.length;  i++) {
              System.out.println(data[i] + "  :  " + sortedData[i]);
              if (sortedData[i].compareTo(data[i]) != 0) {
                  errors++;
              }
          }

          if (errors > 0)
              throw new RuntimeException();
      } finally {
          
          Locale.setDefault(reservedLocale);
      }
  } 

}
