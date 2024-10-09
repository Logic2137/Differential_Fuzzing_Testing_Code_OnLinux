public class CheckPrinterJobSystemProperty {

    public static void main(String[] args) {
        String pjProp = System.getProperty("java.awt.printerjob");
        if (pjProp != null) {
            throw new RuntimeException("pjProp = " + pjProp);
        }
    }
}
