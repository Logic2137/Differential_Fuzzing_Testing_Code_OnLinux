public class CheckGraphicsEnvSystemProperty {

    public static void main(String[] args) {
        String geProp = System.getProperty("java.awt.graphicsenv");
        if (geProp != null) {
            throw new RuntimeException("geProp = " + geProp);
        }
    }
}
