import java.lang.reflect.*;

public class CharType {

    public static void main(String[] args) throws Exception {
        Proxy.newProxyInstance(CharMethod.class.getClassLoader(), new Class[] { CharMethod.class }, new H());
    }

    static interface CharMethod {

        void setChar(char c);
    }

    static class H implements InvocationHandler {

        public Object invoke(Object o, Method m, Object[] arr) {
            return null;
        }
    }
}
