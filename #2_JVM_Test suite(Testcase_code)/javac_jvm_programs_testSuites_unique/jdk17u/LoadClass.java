public class LoadClass {

    public static void main(String[] args) {
        Class c = null;
        try {
            c = Class.forName(args[0]);
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
        if (c != null) {
            System.out.println(c + " loaded.");
        }
    }
}
