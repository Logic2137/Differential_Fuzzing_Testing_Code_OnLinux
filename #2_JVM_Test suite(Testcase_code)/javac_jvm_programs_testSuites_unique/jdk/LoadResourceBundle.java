

package jar1;

import java.io.InputStream;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoadResourceBundle {

        public LoadResourceBundle() throws Exception {
                ResourceBundle bundle;
                InputStream in;

                in = getClass().getResourceAsStream("bundle.properties");
                in.available();

                bundle = ResourceBundle.getBundle("jar1/bundle", Locale.getDefault());
                bundle.getString("Foo");
        }
}
