import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

public final class OverloadedSetter {

    class AAA {
    }

    class CCC extends AAA {
    }

    class BBB extends CCC {
    }

    class DDD extends CCC {
    }

    class ZZZ {
    }

    class ParentADC<T> {

        public void setValue(AAA value) {
        }

        public void setValue(DDD value) {
        }

        public void setValue(CCC value) {
        }
    }

    class ParentACD<T> {

        public void setValue(AAA value) {
        }

        public void setValue(CCC value) {
        }

        public void setValue(DDD value) {
        }
    }

    class ParentDAC<T> {

        public void setValue(DDD value) {
        }

        public void setValue(AAA value) {
        }

        public void setValue(CCC value) {
        }
    }

    class ParentDCA<T> {

        public void setValue(DDD value) {
        }

        public void setValue(CCC value) {
        }

        public void setValue(AAA value) {
        }
    }

    class ParentCAD<T> {

        public void setValue(CCC value) {
        }

        public void setValue(AAA value) {
        }

        public void setValue(DDD value) {
        }
    }

    class ParentCDA<T> {

        public void setValue(CCC value) {
        }

        public void setValue(DDD value) {
        }

        public void setValue(AAA value) {
        }
    }

    class ParentCDAZ<T> {

        public void setValue(CCC value) {
        }

        public void setValue(DDD value) {
        }

        public void setValue(AAA value) {
        }

        public void setValue(ZZZ value) {
        }
    }

    class ParentDACB<T> {

        public DDD getValue() {
            return null;
        }

        public void setValue(AAA value) {
        }

        public void setValue(DDD value) {
        }

        public void setValue(CCC value) {
        }

        public void setValue(BBB value) {
        }
    }

    public static void main(String[] args) throws Exception {
        test(ParentADC.class);
        test(ParentACD.class);
        test(ParentDAC.class);
        test(ParentDCA.class);
        test(ParentCAD.class);
        test(ParentCDA.class);
        test(ParentCDAZ.class);
        test(ParentDACB.class);
    }

    private static void test(Class<?> beanClass) throws Exception {
        BeanInfo beanInfo = Introspector.getBeanInfo(beanClass, Object.class);
        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
        if (pds.length != 1) {
            throw new RuntimeException("Wrong number of properties");
        }
        PropertyDescriptor pd = pds[0];
        String name = pd.getName();
        if (!name.equals("value")) {
            throw new RuntimeException("Wrong name: " + name);
        }
        Class<?> propertyType = pd.getPropertyType();
        if (propertyType != DDD.class) {
            throw new RuntimeException("Wrong property type: " + propertyType);
        }
        Method writeMethod = pd.getWriteMethod();
        if (writeMethod == null) {
            throw new RuntimeException("Write method is null");
        }
        Class<?>[] parameterTypes = writeMethod.getParameterTypes();
        if (parameterTypes.length != 1) {
            throw new RuntimeException("Wrong parameters " + parameterTypes);
        }
        if (parameterTypes[0] != DDD.class) {
            throw new RuntimeException("Wrong type: " + parameterTypes[0]);
        }
    }
}
