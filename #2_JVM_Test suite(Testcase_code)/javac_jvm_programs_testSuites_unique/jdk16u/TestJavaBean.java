

import java.awt.event.ActionListener;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.JavaBean;


public class TestJavaBean {

    static final String DSCR = "description";
    static final String PRP = "value";
    static final String ACT = "action";

    public static void main(final String[] args) throws Exception {
        test(X.class, "TestJavaBean$X", "TestJavaBean$X", null, null);
        test(D.class, "TestJavaBean$D", DSCR, null, null);
        test(DP.class, "TestJavaBean$DP", "TestJavaBean$DP", PRP, null);
        test(DES.class, "TestJavaBean$DES", "TestJavaBean$DES", null, ACT);
        test(DDP.class, "TestJavaBean$DDP", DSCR, PRP, null);
        test(DDES.class, "TestJavaBean$DDES", DSCR, null, ACT);
        test(DPDES.class, "TestJavaBean$DPDES", "TestJavaBean$DPDES", PRP, ACT);
        test(DDPDES.class, "TestJavaBean$DDPDES", DSCR, PRP, ACT);
    }

    private static void test(Class<?> type, String name, String descr,
                             String prop, String event) throws Exception {
        BeanInfo info = Introspector.getBeanInfo(type);
        BeanDescriptor bd = info.getBeanDescriptor();

        if (!bd.getName().equals(name)) {
            throw new Error("unexpected name of the bean");
        }

        if (!bd.getShortDescription().equals(descr)) {
            throw new Error("unexpected description of the bean");
        }

        int dp = info.getDefaultPropertyIndex();
        if (dp < 0 && prop != null) {
            throw new Error("unexpected index of the default property");
        }
        if (dp >= 0) {
            if (!info.getPropertyDescriptors()[dp].getName().equals(prop)) {
                throw new Error("unexpected default property");
            }
        }
        int des = info.getDefaultEventIndex();
        if (des < 0 && event != null) {
            throw new Error("unexpected index of the default event set");
        }
        if (des >= 0) {
            if (!info.getEventSetDescriptors()[des].getName().equals(event)) {
                throw new Error("unexpected default event set");
            }
        }
    }

    public static class X {
    }

    @JavaBean(description = DSCR)
    public static class D {
    }

    @JavaBean(defaultProperty = PRP)
    public static class DP {
        private int value;

        public int getValue() {
            return this.value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    @JavaBean(defaultEventSet = ACT)
    public static class DES {
        public void addActionListener(ActionListener listener) {
        }

        public void removeActionListener(ActionListener listener) {
        }
    }

    @JavaBean(description = DSCR, defaultProperty = PRP)
    public static class DDP {
        private int value;

        public int getValue() {
            return this.value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    @JavaBean(description = DSCR, defaultEventSet = ACT)
    public static class DDES {
        public void addActionListener(ActionListener listener) {
        }

        public void removeActionListener(ActionListener listener) {
        }
    }

    @JavaBean(defaultProperty = PRP, defaultEventSet = ACT)
    public static class DPDES {
        private int value;

        public int getValue() {
            return this.value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public void addActionListener(ActionListener listener) {
        }

        public void removeActionListener(ActionListener listener) {
        }
    }

    @JavaBean(description = DSCR, defaultProperty = PRP, defaultEventSet = ACT)
    public static class DDPDES {
        private int value;

        public int getValue() {
            return this.value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public void addActionListener(ActionListener listener) {
        }

        public void removeActionListener(ActionListener listener) {
        }
    }
}
