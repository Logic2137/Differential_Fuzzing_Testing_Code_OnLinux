public class TestForName {

    public static void main(String[] args) {
        try {
            Class cls = Class.forName("xxx");
            System.out.println("Class = " + cls.getName());
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
    }
}
