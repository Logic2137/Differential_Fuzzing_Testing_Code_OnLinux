



import java.io.File;
import java.util.*;

public class Bug6912560 {
    public static void main(String[] args) {
        
        String tzname = "Asia/Tokyo";
        System.setProperty("user.timezone", tzname);
        
        
        TimeZone.setDefault(null);

        System.setSecurityManager(new SecurityManager());
        TimeZone tz = TimeZone.getDefault();
        if (!tzname.equals(tz.getID())) {
            throw new RuntimeException("got " + tz.getID()
                                       + ", expected " + tzname);
        }
    }
}
