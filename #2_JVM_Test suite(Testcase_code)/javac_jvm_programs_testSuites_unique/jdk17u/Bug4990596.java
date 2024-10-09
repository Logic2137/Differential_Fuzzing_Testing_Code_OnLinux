import java.text.DecimalFormat;

public class Bug4990596 {

    public static void main(String[] args) {
        new DecimalFormat().format(new MutableInteger(0));
    }

    @SuppressWarnings("serial")
    public static class MutableInteger extends Number {

        public int value;

        public MutableInteger() {
        }

        public MutableInteger(int value) {
            this.value = value;
        }

        public double doubleValue() {
            return this.value;
        }

        public float floatValue() {
            return this.value;
        }

        public int intValue() {
            return this.value;
        }

        public long longValue() {
            return this.value;
        }
    }
}
