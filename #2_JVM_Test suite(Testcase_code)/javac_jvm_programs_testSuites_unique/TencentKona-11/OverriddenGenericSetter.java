

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;


public final class OverriddenGenericSetter {

    static class Parent<T> {
        private T value;
        public final T getValue() {return value;}
        protected void setValue(T value) {this.value = value;}
    }

    static class ChildO extends Parent<Object> {
        public ChildO() {}
        @Override
        public void setValue(Object value) {super.setValue(value);}
    }

    
    
    
    static class ChildA extends Parent<ArithmeticException> {
        public ChildA() {}
        @Override
        public void setValue(ArithmeticException value) {super.setValue(value);}
    }

    static class ChildS extends Parent<String> {
        public ChildS() {}
        @Override
        public void setValue(String value) {super.setValue(value);}
    }

    public static void main(String[] args) throws Exception {
        testBehaviour(ChildA.class);
        testBehaviour(ChildO.class);
        testBehaviour(ChildS.class);
        testProperty(ChildA.class, ArithmeticException.class);
        testProperty(ChildO.class, Object.class);
        testProperty(ChildS.class, String.class);
    }

    private static void testBehaviour(Class<?> beanClass) throws Exception {
        BeanInfo beanInfo = Introspector.getBeanInfo(beanClass, Object.class);
        PropertyDescriptor pd = beanInfo.getPropertyDescriptors()[0];
        Object bean = beanClass.getConstructor().newInstance();
        Method readMethod = pd.getReadMethod();
        Method writeMethod = pd.getWriteMethod();

        
        writeMethod.invoke(bean,readMethod.invoke(bean));
        writeMethod.invoke(bean,readMethod.invoke(bean));

        
        
        Object param = pd.getPropertyType().getConstructor().newInstance();
        writeMethod.invoke(bean, param);
        writeMethod.invoke(bean, readMethod.invoke(bean));
        writeMethod.invoke(bean, readMethod.invoke(bean));
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
        Method readMethod = pd.getReadMethod();
        if (readMethod == null) {
            throw new RuntimeException("Read method is null");
        }
        Class<?> returnType = readMethod.getReturnType();
        if (returnType != Object.class) {
            throw new RuntimeException("Wrong return type; " + returnType);
        }
        Method writeMethod = pd.getWriteMethod();
        if (writeMethod == null) {
            throw new RuntimeException("Write method is null");
        }
        Class<?>[] parameterTypes = writeMethod.getParameterTypes();
        if (parameterTypes.length != 1) {
            throw new RuntimeException("Wrong parameters " + parameterTypes);
        }
        if (parameterTypes[0] != type) {
            throw new RuntimeException("Wrong type: " + parameterTypes[0]);
        }
    }
}
