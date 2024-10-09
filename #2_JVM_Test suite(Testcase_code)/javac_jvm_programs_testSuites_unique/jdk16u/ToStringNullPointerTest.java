

 

import java.awt.datatransfer.DataFlavor;

public class ToStringNullPointerTest {

     static DataFlavor df1;

     public static void main(String[] args) {
         df1 = new DataFlavor();
         try {
             String thisDF = df1.toString();
         } catch (NullPointerException e) {
             throw new RuntimeException("Test FAILED: it still throws NPE!");
         }
     }
}

