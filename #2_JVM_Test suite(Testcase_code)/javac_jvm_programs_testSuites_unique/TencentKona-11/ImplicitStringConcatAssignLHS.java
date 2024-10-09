


import java.lang.StringBuilder;

public class ImplicitStringConcatAssignLHS {

    static final int ARR_SIZE = 10; 

    static int x;

    public static void main(String[] args) throws Exception {
        {
          x = 0;
            Object[] arr = new Object[ARR_SIZE];
            arr[x++] += "foo";
            check(1, "plain-plain Object[]");
        }

        {
          x = 0;
            getObjArray()[x++] += "foo";
            check(2, "method-plain Object[]");
        }

        {
          x = 0;
            getObjArray()[getIndex()] += "foo";
            check(2, "method-method Object[]");
        }

        {
            x = 0;
            String[] arr = new String[ARR_SIZE];
            arr[x++] += "foo";
            check(1, "plain-plain String[]");
    }

        {
            x = 0;
            getStringArray()[x++] += "foo";
            check(2, "method-plain String[]");
        }

        {
            x = 0;
            getStringArray()[getIndex()] += "foo";
            check(2, "method-method String[]");
        }

        {
            x = 0;
            CharSequence[] arr = new CharSequence[ARR_SIZE];
            arr[x++] += "foo";
            check(1, "plain-plain CharSequence[]");
        }

        {
            x = 0;
            getCharSequenceArray()[x++] += "foo";
            check(2, "method-plain CharSequence[]");
        }

        {
            x = 0;
            getCharSequenceArray()[getIndex()] += "foo";
            check(2, "method-method CharSequence[]");
        }

        {
            x = 0;
            new MyClass().s += "foo";
            check(1, "MyClass::new (String)");
        }

        {
            x = 0;
            getMyClass().s += "foo";
            check(1, "method MyClass::new (String)");
        }

        {
            x = 0;
            new MyClass().o += "foo";
            check(1, "MyClass::new (object)");
        }

        {
            x = 0;
            getMyClass().o += "foo";
            check(1, "method MyClass::new (object)");
        }
    }

    public static void check(int expected, String label) {
        if (x != expected) {
           StringBuilder sb = new StringBuilder();
           sb.append(label);
           sb.append(": ");
           sb.append("Expected = ");
           sb.append(expected);
           sb.append("actual = ");
           sb.append(x);
           throw new IllegalStateException(sb.toString());
        }
    }

    public static int getIndex() {
       return x++;
    }

    public static class MyClass {
        Object o;
        String s;

        public MyClass() {
       x++;
        }
    }

    public static MyClass getMyClass() {
        return new MyClass();
}

    public static Object[] getObjArray() {
        x++;
        return new Object[ARR_SIZE];
    }

    public static String[] getStringArray() {
        x++;
        return new String[ARR_SIZE];
    }

    public static CharSequence[] getCharSequenceArray() {
        x++;
        return new String[ARR_SIZE];
    }

}

