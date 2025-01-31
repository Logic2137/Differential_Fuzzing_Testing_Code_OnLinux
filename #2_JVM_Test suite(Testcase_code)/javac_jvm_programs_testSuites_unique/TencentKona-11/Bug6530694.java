

import java.util.Locale;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

public class Bug6530694 {
    Bug6530694() {
        Locale.setDefault(Locale.GERMANY);
        UIDefaults defs = UIManager.getDefaults();
        defs.addResourceBundle("Bug6530694");
        String str = defs.getString("testkey");
        if (!"testvalue".equals(str)) {
            throw new RuntimeException("Could not load the resource for de_DE locale");
        }
    }

    public static void main(String[] args) {
        Locale reservedLocale = Locale.getDefault();
        try {
            new Bug6530694();
        } finally {
            
            Locale.setDefault(reservedLocale);
        }
    }
}
