



public class GetAnnotatedTypeTest {

    public void meth(Object param) {}

    public static void main(String[] args) throws NoSuchMethodException {
        if (GetAnnotatedTypeTest.class.getMethod("meth", Object.class).getParameters()[0].getAnnotatedType().getType() != Object.class)
            throw new RuntimeException("Parameter did not have the expected annotated type");
    }
}
