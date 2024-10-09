public class SunBootClassPath {

    public static void main(String[] args) throws Exception {
        if (System.getProperty("sun.boot.class.path") != null) {
            throw new RuntimeException("Test failed, sun.boot.class.path has value: " + System.getProperty("sun.boot.class.path"));
        } else {
            System.out.println("Test SunBootClassPath passed");
        }
    }
}
