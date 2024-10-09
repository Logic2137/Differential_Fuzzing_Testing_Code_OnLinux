

 
import java.util.Locale;
import javax.management.modelmbean.DescriptorSupport;

public class DescriptorSupportXMLLocaleTest {

    public static void main(String[] args) throws Exception {
        boolean failed = false;
        String xmlDesc = "<DESCRIPTOR>"
                + "<FIELD name=\"field1\" value=\"dummy\">"
                + "</FIELD>"
                + "</DESCRIPTOR>";
        Locale loc = Locale.getDefault();
        try {
            Locale.setDefault(new Locale("tr", "TR"));
            new DescriptorSupport(xmlDesc);
        } catch (Exception e) {
            e.printStackTrace(System.out);
            failed = true;
        }finally{
            Locale.setDefault(loc);
        }

        if (!failed) {
            System.out.println("OK: all tests passed");
        } else {
            System.out.println("TEST FAILED");
            throw new IllegalArgumentException("Test Failed");
        }
    }
}

