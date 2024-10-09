



public class ShowClassLoader {

    public static void main(String[] args) {
        Object[] oa = new Object[0];
        ShowClassLoader[] sa = new ShowClassLoader[0];

        System.out.println("Classloader for Object[] is " + oa.getClass().getClassLoader());
        System.out.println("Classloader for SCL[] is " + sa.getClass().getClassLoader() );

        if (sa.getClass().getClassLoader() == null) {
            throw new RuntimeException("Wrong class loader");
        }
    }
}
