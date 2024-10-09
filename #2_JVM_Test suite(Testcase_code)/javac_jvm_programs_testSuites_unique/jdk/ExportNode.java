




import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class ExportNode {
    private static final String NODE_NAME_1 = "ExportNodeTestName1";
    private static final String NODE_NAME_2 = "ExportNodeTestName2";

    public static void main(String[] args) throws
        BackingStoreException, IOException {
        Preferences N1 = Preferences.userRoot().node("ExportNodeTest1");
        N1.put(NODE_NAME_1,"ExportNodeTestValue1");
        Preferences N2 = N1.node("ExportNodeTest2");
        N2.put(NODE_NAME_2,"ExportNodeTestValue2");
        ByteArrayOutputStream exportStream = new ByteArrayOutputStream();
        N2.exportNode(exportStream);

        
        
        N1.removeNode();

        String streamAsString = exportStream.toString("UTF-8");

        StringBuilder sb = null;
        if (streamAsString.lastIndexOf(NODE_NAME_2) == -1) {
            if (sb == null)
                sb = new StringBuilder();
            sb.append(NODE_NAME_2 + " should have been found");
        }
        if (streamAsString.lastIndexOf(NODE_NAME_1) != -1) {
            if (sb == null)
                sb = new StringBuilder();
            else
                sb.append("; ");
            sb.append(NODE_NAME_1 + " should *not* have been found");
        }

        if (sb != null)
            throw new RuntimeException(sb.toString());
   }
}
