

import java.io.Serializable;
import java.lang.invoke.*;
import java.util.concurrent.Callable;


public class StringConcatFactoryEmptyMethods {

    public static void main(String[] args) throws Throwable {
        StringConcatFactory.makeConcat(
            MethodHandles.lookup(),
            "foo",
            MethodType.methodType(String.class)
        );

        StringConcatFactory.makeConcatWithConstants(
            MethodHandles.lookup(),
            "foo",
            MethodType.methodType(String.class),
            ""
        );
    }

}
