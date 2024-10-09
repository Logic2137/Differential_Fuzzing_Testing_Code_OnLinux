public class SetPropertiesNull {

    public static void main(String[] args) {
        final String version = System.getProperty("java.version");
        System.setProperties(null);
        final String newVersion = System.getProperty("java.version");
        if (!version.equals(newVersion)) {
            throw new RuntimeException("java.version differs: '" + version + "'  '" + newVersion + "'");
        }
    }
}
