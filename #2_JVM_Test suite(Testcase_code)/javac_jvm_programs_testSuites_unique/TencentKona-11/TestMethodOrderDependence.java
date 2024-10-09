

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;


public final class TestMethodOrderDependence {

    public static class Base {

        public Object getI() {
            return null;
        }

        public Object getE() {
            return null;
        }
    }

    public static class Super extends Base {

        public Number getI() {
            return null;
        }
        public Comparable<?> getE() {
            return null;
        }
    }

    public static class Sub extends Super {

        public Integer getI() {
            return null;
        }

        public void setFoo(Character foo) {
        }

        public void setFoo(String foo) {
        }

        public void setFoo(Object[] foo) {
        }

        public void setFoo(Enum foo) {
        }

        public void setFoo(Long foo) {
        }

        public void setFoo(Long[] foo) {
        }

        public void setFoo(Long foo, int i) {
        }

        public void setFoo(Object foo) {
        }

        public void setFoo(AbstractList foo) {
        }

        public void setFoo(ArrayList foo) {
        }

        public void setFoo(Integer foo) {
        }

        public void setFoo(Number foo) {
        }

        public void setFoo(Comparable<?> foo) {
        }

        public void setFoo(Serializable foo) {
        }

        public void setFoo(Vector<?> foo) {
        }

        public void setFoo(long foo) {
        }

        public void setFoo(int foo) {
        }

        public Enum getE() {
            return null;
        }

        public void setE(Enum e) {
        }

        public void setE(Float e) {
        }

        public void setE(Long e) {
        }
    }

    public static void main(final String[] args) throws Exception {
        final BeanInfo beanInfo = Introspector.getBeanInfo(Sub.class);
        final PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
        for (final PropertyDescriptor pd : pds) {
            System.err.println("pd = " + pd);
            final Class<?> type = pd.getPropertyType();
            if (type != Class.class && type != Long[].class
                    && type != Integer.class && type != Enum.class) {
                throw new RuntimeException(Arrays.toString(pds));
            }
        }
    }
}
