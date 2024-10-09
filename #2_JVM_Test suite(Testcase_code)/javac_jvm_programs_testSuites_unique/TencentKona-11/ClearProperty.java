public class ClearProperty {

    public static void main(String[] argv) throws Exception {
        clearTest();
        paramTest();
    }

    static void clearTest() throws Exception {
        System.setProperty("blah", "blech");
        if (!System.getProperty("blah").equals("blech"))
            throw new RuntimeException("Clear test failed 1");
        System.clearProperty("blah");
        if (System.getProperty("blah") != null)
            throw new RuntimeException("Clear test failed 2");
    }

    static void paramTest() throws Exception {
        try {
            System.clearProperty(null);
            throw new RuntimeException("Param test failed");
        } catch (NullPointerException npe) {
        }
        try {
            System.clearProperty("");
            throw new RuntimeException("Param test failed");
        } catch (IllegalArgumentException iae) {
        }
    }
}
