import java.util.Properties;

public class SetProperties {

    public static void main(String[] argv) {
        System.setProperties((Properties) null);
        System.getProperties().containsKey("java.version");
    }
}
