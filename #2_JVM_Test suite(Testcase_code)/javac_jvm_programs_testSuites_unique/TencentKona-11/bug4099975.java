



import java.text.*;
import java.util.Date;
public class bug4099975 {
    public static void main (String args[]){
        DateFormatSymbols symbols = new DateFormatSymbols();
        SimpleDateFormat df = new SimpleDateFormat("E hh:mm", symbols);
        System.out.println(df.toLocalizedPattern());
        symbols.setLocalPatternChars("abcdefghijklmonpqr"); 
        System.out.println(df.toLocalizedPattern());

    }
}
