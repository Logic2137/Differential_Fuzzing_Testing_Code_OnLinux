



import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.Arrays;

public class InheritedAssociatedAnnotations {

    public static void main(String[] args) {
        checkAssociated(A3.class);
        checkAssociated(B3.class);
        checkAssociated(C3.class);
        checkAssociated(D3.class);
    }

    private static void checkAssociated(AnnotatedElement ae) {
        Ann[] actual = ae.getAnnotationsByType(Ann.class);
        Ann[] expected = ae.getAnnotation(ExpectedAssociated.class).value();

        if (!Arrays.equals(actual, expected)) {
            throw new RuntimeException(String.format(
                    "Test failed for %s: Expected %s but got %s.",
                    ae,
                    Arrays.toString(expected),
                    Arrays.toString(actual)));
        }
    }

}

@Retention(RetentionPolicy.RUNTIME)
@interface ExpectedAssociated {
    Ann[] value();
}


@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(AnnCont.class)
@interface Ann {
    int value();
}

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface AnnCont {
    Ann[] value();
}


@Ann(10)
class A1 {}

@Ann(20)
class A2 extends A1 {}

@ExpectedAssociated({@Ann(20)})
class A3 extends A2 {}


@Ann(10) @Ann(11)
class B1 {}

@Ann(20)
class B2 extends B1 {}

@ExpectedAssociated({@Ann(20)})
class B3 extends B2 {}


@Ann(10)
class C1 {}

@Ann(20) @Ann(21)
class C2 extends C1 {}

@ExpectedAssociated({@Ann(20), @Ann(21)})
class C3 extends C2 {}


@Ann(10) @Ann(11)
class D1 {}

@Ann(20) @Ann(21)
class D2 extends D1 {}

@ExpectedAssociated({@Ann(20), @Ann(21)})
class D3 extends D2 {}
