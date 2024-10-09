import sun.management.ManagementFactoryHelper;
import com.sun.management.DiagnosticCommandMBean;

public class DcmdUtil {

    public static String executeDcmd(String cmd, String... args) {
        DiagnosticCommandMBean dcmd = ManagementFactoryHelper.getDiagnosticCommandMBean();
        Object[] dcmdArgs = { args };
        String[] signature = { String[].class.getName() };
        try {
            System.out.print("> " + cmd + " ");
            for (String s : args) {
                System.out.print(s + " ");
            }
            System.out.println(":");
            String result = (String) dcmd.invoke(transform(cmd), dcmdArgs, signature);
            System.out.println(result);
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static String transform(String name) {
        StringBuilder sb = new StringBuilder();
        boolean toLower = true;
        boolean toUpper = false;
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (c == '.' || c == '_') {
                toLower = false;
                toUpper = true;
            } else {
                if (toUpper) {
                    toUpper = false;
                    sb.append(Character.toUpperCase(c));
                } else if (toLower) {
                    sb.append(Character.toLowerCase(c));
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }
}
