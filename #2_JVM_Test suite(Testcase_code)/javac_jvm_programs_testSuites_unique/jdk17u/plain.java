
package COM.foo.content.text;

import java.net.ContentHandler;
import java.io.InputStream;
import java.net.URLConnection;
import java.io.IOException;

public class plain extends ContentHandler {

    public Object getContent(URLConnection uc) {
        try {
            InputStream is = uc.getInputStream();
            StringBuffer sb = new StringBuffer();
            int c;
            sb.append("[Content of " + uc.getURL() + "]\n\n");
            sb.append("[This opening message brought to you by your plain/text\n");
            sb.append("content handler. To remove this content handler, delete the\n");
            sb.append("COM.foo.content.text directory from your class path and\n");
            sb.append("the java.content.handler.pkgs property from your HotJava\n");
            sb.append("properties file.]\n");
            sb.append("----------------------------------------------------------------\n\n");
            while ((c = is.read()) >= 0) {
                sb.append((char) c);
            }
            is.close();
            return sb.toString();
        } catch (IOException e) {
            return "Problem reading document: " + uc.getURL();
        }
    }
}
