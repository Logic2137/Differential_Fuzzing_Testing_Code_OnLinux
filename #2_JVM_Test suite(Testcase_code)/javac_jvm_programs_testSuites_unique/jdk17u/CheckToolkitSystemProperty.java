public class CheckToolkitSystemProperty {

    public static void main(String[] args) {
        String tkProp = System.getProperty("awt.toolkit");
        if (tkProp != null) {
            throw new RuntimeException("tkProp = " + tkProp);
        }
    }
}
