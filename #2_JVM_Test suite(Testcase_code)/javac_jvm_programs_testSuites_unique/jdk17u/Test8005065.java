import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;
import java.beans.EventSetDescriptor;
import java.beans.ExceptionListener;
import java.beans.Expression;
import java.beans.Statement;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;

public class Test8005065 {

    public static void main(String[] args) {
        testDefaultPersistenceDelegate();
        testEventSetDescriptor();
        testMethodDescriptor();
        testStatement();
    }

    private static void testDefaultPersistenceDelegate() {
        Encoder encoder = new Encoder();
        String[] array = { "array" };
        MyDPD dpd = new MyDPD(array);
        dpd.instantiate(dpd, encoder);
        array[0] = null;
        dpd.instantiate(dpd, encoder);
    }

    private static void testEventSetDescriptor() {
        try {
            MethodDescriptor[] array = { new MethodDescriptor(MyDPD.class.getMethod("getArray")) };
            EventSetDescriptor descriptor = new EventSetDescriptor(null, null, array, null, null);
            test(descriptor.getListenerMethodDescriptors());
            array[0] = null;
            test(descriptor.getListenerMethodDescriptors());
            descriptor.getListenerMethodDescriptors()[0] = null;
            test(descriptor.getListenerMethodDescriptors());
        } catch (Exception exception) {
            throw new Error("unexpected error", exception);
        }
    }

    private static void testMethodDescriptor() {
        try {
            ParameterDescriptor[] array = { new ParameterDescriptor() };
            MethodDescriptor descriptor = new MethodDescriptor(MyDPD.class.getMethod("getArray"), array);
            test(descriptor.getParameterDescriptors());
            array[0] = null;
            test(descriptor.getParameterDescriptors());
            descriptor.getParameterDescriptors()[0] = null;
            test(descriptor.getParameterDescriptors());
        } catch (Exception exception) {
            throw new Error("unexpected error", exception);
        }
    }

    private static void testStatement() {
        Object[] array = { new Object() };
        Statement statement = new Statement(null, null, array);
        test(statement.getArguments());
        array[0] = null;
        test(statement.getArguments());
        statement.getArguments()[0] = null;
        test(statement.getArguments());
    }

    private static <T> void test(T[] array) {
        if (array.length != 1) {
            throw new Error("unexpected array length");
        }
        if (array[0] == null) {
            throw new Error("unexpected array content");
        }
    }

    public static class MyDPD extends DefaultPersistenceDelegate implements ExceptionListener {

        private final String[] array;

        public MyDPD(String[] array) {
            super(array);
            this.array = array;
        }

        public Expression instantiate(Object instance, Encoder encoder) {
            encoder.setExceptionListener(this);
            return super.instantiate(instance, encoder);
        }

        public String[] getArray() {
            return this.array;
        }

        public void exceptionThrown(Exception exception) {
            throw new Error("unexpected error", exception);
        }
    }
}
