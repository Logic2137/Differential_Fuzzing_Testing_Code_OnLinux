

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.StringConcatFactory;


public class StringConcatFactoryRepeatedConstants {

    public static void main(String[] args) throws Throwable {

        CallSite site = StringConcatFactory.makeConcatWithConstants(
            MethodHandles.lookup(),
            "foo",
            MethodType.methodType(String.class),
            "\u0002\u0002",
            "foo", "bar"
        );
        String string = (String)site.dynamicInvoker().invoke();
        if (!"foobar".equals(string)) {
            throw new IllegalStateException("Expected: foobar, got: " + string);
        }

        site = StringConcatFactory.makeConcatWithConstants(
                MethodHandles.lookup(),
                "foo",
                MethodType.methodType(String.class),
                "\u0002\u0002test\u0002\u0002",
                "foo", 17.0f, 4711L, "bar"
        );
        string = (String)site.dynamicInvoker().invoke();
        StringBuilder sb = new StringBuilder();
        sb.append("foo").append(17.0f).append("test").append(4711L).append("bar");
        if (!sb.toString().equals(string)) {
            throw new IllegalStateException("Expected: " + sb.toString() + ", got: " + string);
        }
    }

}
