

package nsk.share;

import java.util.Properties;


public class PrintProperties {
        public static void main(String[] args) {
                Properties pr = System.getProperties();
                switch (args.length) {
                case 0:
                        pr.list(System.out);
                        System.exit(0);
                case 1:
                        String value = pr.getProperty(args[0]);
                        if (value == null) {
                                System.err.println("Not found");
                                System.exit(1);
                        } else {
                                System.out.println(value);
                                System.exit(0);
                        }
                default:
                        System.out.println("Usage:");
                        System.out.println("    PrintProperties");
                        System.out.println("    PrintProperties <property name>");
                        System.exit(255);
                }
        }
}
