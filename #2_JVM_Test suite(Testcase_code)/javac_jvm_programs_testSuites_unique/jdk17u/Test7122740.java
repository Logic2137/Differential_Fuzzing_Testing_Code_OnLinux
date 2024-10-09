import java.beans.PropertyDescriptor;

public class Test7122740 {

    public static void main(String[] args) throws Exception {
        long time = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            new PropertyDescriptor("name", PropertyDescriptor.class);
            new PropertyDescriptor("value", Concrete.class);
        }
        time -= System.nanoTime();
        System.out.println("Time (ms): " + (-time / 1000000));
    }

    public static class Abstract<T> {

        private T value;

        public T getValue() {
            return this.value;
        }

        public void setValue(T value) {
            this.value = value;
        }
    }

    private static class Concrete extends Abstract<String> {
    }
}
