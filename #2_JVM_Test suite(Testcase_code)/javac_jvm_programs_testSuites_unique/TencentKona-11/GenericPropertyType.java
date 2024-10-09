

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;


public final class GenericPropertyType {

    
    static class ParentNo<T> {
        public T getValue() {return null;}
        public void setValue(T value) {}
    }

    static class ChildNoO extends ParentNo<Object> {}
    static class ChildNoA extends ParentNo<ArithmeticException> {}
    static class ChildNoS extends ParentNo<String> {}

    
    static class ParentNoGet<T> {
        protected void setValue(T value) {}
    }

    static class ChildNoGetO extends ParentNoGet<Object> {
        @Override
        public void setValue(Object value) {}
    }

    static class ChildNoGetA extends ParentNoGet<ArithmeticException> {
        @Override
        public void setValue(ArithmeticException value) {}
    }

    static class ChildNoGetS extends ParentNoGet<String> {
        @Override
        public void setValue(String value) {}
    }

    
    static class ParentGet<T> {
        public final T getValue() {return null;}
        protected void setValue(T value) {}
    }

    static class ChildGetO extends ParentGet<Object> {
        @Override
        public void setValue(Object value) {}
    }

    static class ChildGetA extends ParentGet<ArithmeticException> {
        @Override
        public void setValue(ArithmeticException value) {}
    }

    static class ChildGetS extends ParentGet<String> {
        @Override
        public void setValue(String value) {}
    }

    
    static class ParentAll<T> {
        protected T getValue() {return null;}
        protected void setValue(T value) {}
    }

    static class ChildAllO extends ParentAll<Object> {
        @Override
        public void setValue(Object value) {}
        @Override
        public Object getValue() {return null;}
    }

    static class ChildAllA extends ParentAll<ArithmeticException> {
        @Override
        public void setValue(ArithmeticException value) {}
        @Override
        public ArithmeticException getValue() {return null;}
    }

    static class ChildAllS extends ParentAll<String> {
        @Override
        public void setValue(String value) {}
        @Override
        public String getValue() {return null;}
    }

    public static void main(String[] args) throws Exception {
        testProperty(ChildNoGetA.class, ArithmeticException.class);
        testProperty(ChildNoGetO.class, Object.class);
        testProperty(ChildNoGetS.class, String.class);

        testProperty(ChildGetA.class, ArithmeticException.class);
        testProperty(ChildGetO.class, Object.class);
        testProperty(ChildGetS.class, String.class);

        testProperty(ChildAllA.class, ArithmeticException.class);
        testProperty(ChildAllO.class, Object.class);
        testProperty(ChildAllS.class, String.class);

        testProperty(ChildNoA.class, ArithmeticException.class);
        testProperty(ChildNoO.class, Object.class);
        testProperty(ChildNoS.class, String.class);
    }

    private static void testProperty(Class<?> beanClass, Class<?> type) throws Exception {
        BeanInfo beanInfo = Introspector.getBeanInfo(beanClass, Object.class);
        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
        if (pds.length != 1) {
            throw new RuntimeException("Wrong number of properties");
        }
        PropertyDescriptor pd = pds[0];
        System.out.println("pd = " + pd);
        String name = pd.getName();
        if (!name.equals("value")) {
            throw new RuntimeException("Wrong name: " + name);
        }
        Class<?> propertyType = pd.getPropertyType();
        if (propertyType != type) {
            throw new RuntimeException("Wrong property type: " + propertyType);
        }
    }
}
