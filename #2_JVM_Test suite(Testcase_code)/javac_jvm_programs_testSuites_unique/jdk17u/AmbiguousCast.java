class Test {

    private static class GenericWrapper<Elem> {

        private Elem theObject;

        public GenericWrapper(Elem arg) {
            theObject = arg;
        }

        public <T extends Elem> GenericWrapper(GenericWrapper<T> other) {
            this.theObject = other.theObject;
        }

        public String toString() {
            return theObject.toString();
        }
    }

    private static GenericWrapper<String> method(Object wrappedString) {
        return (GenericWrapper<String>) wrappedString;
    }

    public static void main(String[] args) {
        System.out.println(method(new GenericWrapper<String>("abc")));
        System.out.println(method(new GenericWrapper<Exception>(new Exception())));
    }
}
