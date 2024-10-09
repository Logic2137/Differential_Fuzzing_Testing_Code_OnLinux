import javax.print.PrintService;
import javax.print.PrintServiceLookup;

public class PrintValues {

    public static void main(String[] args) {
        PrintService printer = PrintServiceLookup.lookupDefaultPrintService();
        if (printer == null) {
            System.out.println("No default printer configured. Cannot continue");
            return;
        }
        Class[] categories = printer.getSupportedAttributeCategories();
        for (int i = 0; i < categories.length; i++) {
            System.out.print("Class " + categories[i]);
            System.out.print(' ');
            Object value = printer.getSupportedAttributeValues(categories[i], null, null);
            if ((value != null) && (value.getClass().isArray())) {
                Object[] v = (Object[]) value;
                for (int j = 0; j < v.length; j++) {
                    if (j > 0) {
                        System.out.print(", ");
                    }
                    System.out.print(v[j]);
                }
            } else {
                System.out.print(value);
            }
            System.out.println();
        }
    }
}
