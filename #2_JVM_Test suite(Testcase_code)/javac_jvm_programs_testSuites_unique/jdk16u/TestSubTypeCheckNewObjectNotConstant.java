



public class TestSubTypeCheckNewObjectNotConstant {
    public static void main(String[] args) throws CloneNotSupportedException {
        for (int i = 0; i < 20_000; i++) {
            test();
            test_helper1(test_helper2(0));
        }
    }

    private static boolean test() throws CloneNotSupportedException {
        int i = 0;
        for (; i < 10; i++);
        AbstractClass o = test_helper2(i);
        return test_helper1(o);
    }

    private static AbstractClass test_helper2(int i) {
        AbstractClass o;
        if (i == 10) {
            o = new ConcreteSubClass1();
        } else {
            o = new ConcreteSubClass2();
        }
        return o;
    }

    private static boolean test_helper1(AbstractClass o) throws CloneNotSupportedException {
        final Object c = o.clone();
        return c instanceof ConcreteSubClass1;
    }

    static abstract class AbstractClass implements Cloneable{
        @Override
        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

    static class ConcreteSubClass1 extends AbstractClass {
    }

    static class ConcreteSubClass2 extends AbstractClass {
    }
}
