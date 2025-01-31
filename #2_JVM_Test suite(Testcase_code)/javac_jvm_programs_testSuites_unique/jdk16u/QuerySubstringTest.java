



import java.lang.management.ManagementFactory;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.Query;
import javax.management.QueryExp;

public class QuerySubstringTest {

    public static interface SimpleMBean {
        public String getString();
    }

    public static class Simple implements SimpleMBean {
        public Simple(String value) {
            this.value = value;
        }
        public String getString() {
            return value;
        }
        private String value;
    }

    private static String[][] data = {
        { "a*b?c\\d[e-f]",   "OK", "OK", "OK" },
        { "a*b?c\\d[e-f]g",  "OK", "OK", "KO" },
        { "za*b?c\\d[e-f]",  "KO", "OK", "OK" },
        { "za*b?c\\d[e-f]g", "KO", "OK", "KO" },
        { "a*b?c\\de",       "KO", "KO", "KO" },
        { "a*b?c\\deg",      "KO", "KO", "KO" },
        { "za*b?c\\de",      "KO", "KO", "KO" },
        { "za*b?c\\deg",     "KO", "KO", "KO" },
        { "a*b?c\\df",       "KO", "KO", "KO" },
        { "a*b?c\\dfg",      "KO", "KO", "KO" },
        { "za*b?c\\df",      "KO", "KO", "KO" },
        { "za*b?c\\dfg",     "KO", "KO", "KO" },
        { "axxbxc\\de",      "KO", "KO", "KO" },
        { "axxbxc\\deg",     "KO", "KO", "KO" },
        { "zaxxbxc\\de",     "KO", "KO", "KO" },
        { "zaxxbxc\\deg",    "KO", "KO", "KO" },
        { "axxbxc\\df",      "KO", "KO", "KO" },
        { "axxbxc\\dfg",     "KO", "KO", "KO" },
        { "zaxxbxc\\df",     "KO", "KO", "KO" },
        { "zaxxbxc\\dfg",    "KO", "KO", "KO" },
    };

    private static int query(MBeanServer mbs,
                             int type,
                             String substring,
                             String[][] data) throws Exception {

        int error = 0;

        String querySubString = null;
        switch (type) {
            case 1:
                querySubString = "InitialSubString";
                break;
            case 2:
                querySubString = "AnySubString";
                break;
            case 3:
                querySubString = "FinalSubString";
                break;
        }

        System.out.println("\n" + querySubString + " = " + substring + "\n");

        for (int i = 0; i < data.length; i++) {
            ObjectName on = new ObjectName("test:type=Simple,query=" +
                                           querySubString + ",name=" + i);
            Simple s = new Simple(data[i][0]);
            mbs.registerMBean(s, on);
            QueryExp q = null;
            switch (type) {
                case 1:
                    q = Query.initialSubString(Query.attr("String"),
                                               Query.value(substring));
                    break;
                case 2:
                    q = Query.anySubString(Query.attr("String"),
                                           Query.value(substring));
                    break;
                case 3:
                    q = Query.finalSubString(Query.attr("String"),
                                             Query.value(substring));
                    break;
            }
            q.setMBeanServer(mbs);
            boolean r = q.apply(on);
            System.out.print("Attribute Value = " +
                mbs.getAttribute(on, "String"));
            if (r && "OK".equals(data[i][type])) {
                System.out.println(" OK");
            } else if (!r && "KO".equals(data[i][type])) {
                System.out.println(" KO");
            } else {
                System.out.println(" Error");
                error++;
            }
        }

        return error;
    }

    public static void main(String[] args) throws Exception {

        int error = 0;

        String pattern = "a*b?c\\d[e-f]";

        System.out.println(
          "\n--- Test javax.management.Query.{initial|any|final}SubString ---");

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

        error += query(mbs, 1, pattern, data);

        error += query(mbs, 2, pattern, data);

        error += query(mbs, 3, pattern, data);

        if (error > 0) {
            System.out.println("\nTest failed! " + error + " errors.\n");
            throw new IllegalArgumentException("Test failed");
        } else {
            System.out.println("\nTest passed!\n");
        }
    }
}
