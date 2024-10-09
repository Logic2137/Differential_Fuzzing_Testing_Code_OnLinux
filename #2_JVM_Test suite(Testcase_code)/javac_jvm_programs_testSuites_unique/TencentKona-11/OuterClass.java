

package jdk.nashorn.test.models;

@SuppressWarnings("javadoc")
public class OuterClass {
    private final String value;

    public OuterClass(final String value) {
        this.value = value;
    }

    public static class InnerStaticClass {

        public static class InnerInnerStaticClass {
            
        }

        private final String value;

        public InnerStaticClass(final String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "InnerStaticClass[value=" + value + "]";
        }
    }

    public class InnerNonStaticClass {
        private final String val;

        public InnerNonStaticClass(final String value) {
            this.val = value;
        }

        @Override
        public String toString() {
            return "InnerNonStaticClass[value=" + val + ", outer=" + OuterClass.this + "]";
        }
    }

    @Override
    public String toString() {
        return "OuterClass[value=" + value + "]";
    }
}
