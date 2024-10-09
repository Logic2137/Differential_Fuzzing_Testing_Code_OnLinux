import java.lang.annotation.*;

class InvalidClsTypeParamTarget {

    @Target({ ElementType.TYPE_PARAMETER, ElementType.TYPE_USE, ElementType.FIELD })
    @Repeatable(TC.class)
    @interface T {

        int value();
    }

    @Target(ElementType.FIELD)
    @interface TC {

        T[] value();
    }

    class Test<@T(1) @T(2) N> {
    }
}
