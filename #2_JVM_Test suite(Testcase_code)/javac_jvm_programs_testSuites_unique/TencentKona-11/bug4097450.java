



import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class bug4097450
{
    public static void main(String args[])
    {
        
        
        
        String[]  dstring = {"97","1997",  "97","1997","01","2001",  "01","2001"
                             ,  "1",
                             "1","11",  "11","111", "111"};
        String[]  dformat = {"yy",  "yy","yyyy","yyyy","yy",  "yy","yyyy","yyyy"
                             ,
                             "yy","yyyy","yy","yyyy", "yy","yyyy"};
        boolean[] dresult = {true, false, false,  true,true, false, false,  true
                             ,false,
                             false,true, false,false, false};
        SimpleDateFormat formatter;
        SimpleDateFormat resultFormatter = new SimpleDateFormat("yyyy");

        System.out.println("Format\tSource\tResult");
        System.out.println("-------\t-------\t-------");
        for (int i = 0; i < dstring.length; i++)
        {
            System.out.print(dformat[i] + "\t" + dstring[i] + "\t");
            formatter = new SimpleDateFormat(dformat[i]);
            try {
                System.out.print(resultFormatter.format(formatter.parse(dstring[
                                                                                i])));
                
            }
            catch (ParseException exception) {
                
                System.out.print("exception --> " + exception);
            }
            System.out.println();
        }
    }
}
