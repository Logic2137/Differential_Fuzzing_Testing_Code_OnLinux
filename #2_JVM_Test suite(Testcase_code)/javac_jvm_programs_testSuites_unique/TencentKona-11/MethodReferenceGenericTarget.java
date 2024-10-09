



public class MethodReferenceGenericTarget {
    static String result = "";

    interface ISi { int m(Short a); }

    public static void main(String[] args) {
      (new MethodReferenceGenericTarget()).testUnboxObjectToNumberWiden();
      if (!result.equals("7775"))
          throw new AssertionError("Incorrect result");
        MethodReferenceTestPrivateTypeConversion.main(null);
        new InferenceHookTest().test();
    }

    void foo(ISi q) {
        result += q.m((short)75);
    }

    public void testUnboxObjectToNumberWiden() {
        ISi q = (new E<Short>())::xI;
        result += q.m((short)77);
        
        
        foo((new E<Short>())::xI);
    }

    class E<T> {
        private T xI(T t) { return t; }
    }
}


class MethodReferenceTestPrivateTypeConversion {

    class MethodReferenceTestTypeConversion_E<T> {
        private T xI(T t) { return t; }
    }

    interface ISi { int m(Short a); }

    interface ICc { char m(Character a); }

    public void testUnboxObjectToNumberWiden() {
        ISi q = (new MethodReferenceTestTypeConversion_E<Short>())::xI;
        if ((q.m((short)77) != (short)77))
            throw new AssertionError("Incorrect result");
    }

    public void testUnboxObjectToChar() {
        ICc q = (new MethodReferenceTestTypeConversion_E<Character>())::xI;
        if (q.m('@') != '@')
            throw new AssertionError("Incorrect result");
    }

    public static void main(String[] args) {
        new MethodReferenceTestPrivateTypeConversion().testUnboxObjectToNumberWiden();
        new MethodReferenceTestPrivateTypeConversion().testUnboxObjectToChar();
    }
}

class InferenceHookTestBase {
    <X> X m(Integer i) { return null; }
}

class InferenceHookTest extends InferenceHookTestBase {
    interface SAM1<R> {
        R m(Integer i);
    }

    <Z> Z g(SAM1<Z> o) { return null; }

    void test() {
        String s = g(super::m);
        if (s != null)
            throw new AssertionError("Incorrect result");
    }
}
