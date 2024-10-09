



import java.net.*;
import java.util.List;
import java.util.Locale;

public class B6791927 {
    public static final void main(String[] aaParamters) throws Exception {
        Locale reservedLocale = Locale.getDefault();
        try {
            
            Locale.setDefault(Locale.FRANCE);
            List<HttpCookie> cookies = HttpCookie.parse("set-cookie:" +
                    " CUSTOMER=WILE_E_COYOTE;" +
                    " expires=Sat, 09-Nov-2041 23:12:40 GMT");
            if (cookies == null || cookies.isEmpty()) {
                throw new RuntimeException("No cookie found");
            }
            for (HttpCookie c : cookies) {
                if (c.getMaxAge() == 0) {
                    throw new RuntimeException(
                        "Expiration date shouldn't be 0");
                }
            }
        } finally {
            
            Locale.setDefault(reservedLocale);
        }
    }
}
