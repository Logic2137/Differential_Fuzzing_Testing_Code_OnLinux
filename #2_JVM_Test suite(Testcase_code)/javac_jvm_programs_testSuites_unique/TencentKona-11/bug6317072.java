



import java.util.*;
import java.text.*;
import java.io.*;

public class bug6317072 {

    public static void main(String[] args) {

        try {
            new SimpleDateFormat("yy", (Locale)null);
            throw new RuntimeException("should thrown a NullPointerException");
        } catch (NullPointerException e) {
        }

        try {
            new SimpleDateFormat((String)null, Locale.getDefault());
            throw new RuntimeException("should thrown a NullPointerException");
        } catch (NullPointerException e) {
        }

        try {
            new SimpleDateFormat("yy", (DateFormatSymbols)null);
            throw new RuntimeException("should thrown a NullPointerException");
        } catch (NullPointerException e) {
        }

        try {
            new SimpleDateFormat((String)null, DateFormatSymbols.getInstance());
            throw new RuntimeException("should thrown a NullPointerException");
        } catch (NullPointerException e) {
        }

        try {
            DateFormat.getTimeInstance(DateFormat.FULL, null);
            throw new RuntimeException("should thrown a NullPointerException");
        } catch (NullPointerException e) {
        }

        try {
            DateFormat.getDateInstance(DateFormat.FULL, null);
            throw new RuntimeException("should thrown a NullPointerException");
        } catch (NullPointerException e) {
        }

        try {
            DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, null);
            throw new RuntimeException("should thrown a NullPointerException");
        } catch (NullPointerException e) {
        }
    }
}
