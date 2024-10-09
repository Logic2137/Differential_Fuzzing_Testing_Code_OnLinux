



public class SystemPropertyTest {

    public static void main(String[]args) {
        String prop = System.getProperty("apple.awt.application.name");
        if (prop == null) {
            throw new RuntimeException("Property not set");
        }
        if (!prop.equals(args[0])) {
            throw new RuntimeException("Got " + prop + " expected " + args[0]);
        }
    }
}
