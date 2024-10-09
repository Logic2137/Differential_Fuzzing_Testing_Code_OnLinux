import java.lang.annotation.*;

class InvalidMethodTypeParamTarget {

    @Target({ ElementType.TYPE_PARAMETER, ElementType.METHOD })
    @Repeatable(TC.class)
    @interface T {

        int value();
    }

    @Target(ElementType.METHOD)
    @interface TC {

        T[] value();
    }

    public <@T(1) @T(2) N> void method() {
    }
}
