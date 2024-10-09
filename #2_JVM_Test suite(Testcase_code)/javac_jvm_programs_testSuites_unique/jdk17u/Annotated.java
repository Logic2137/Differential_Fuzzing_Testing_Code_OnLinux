
package pkg7;

import java.lang.annotation.Documented;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

interface AnnotatedBase {

    public Iterable<String> m1(Class<? extends CharSequence> p1, int[] p2);
}

interface AnnotatedSub1 extends AnnotatedBase {

    @Override
    public Iterable<String> m1(Class<? extends CharSequence> p1, int[] p2);
}

interface AnnotatedSub2 extends AnnotatedBase {

    @Override
    @A
    public Iterable<String> m1(Class<? extends CharSequence> p1, int[] p2);
}

interface AnnotatedSub3 extends AnnotatedBase {

    @Override
    @A
    public Iterable<String> m1(Class<? extends CharSequence> p1, int[] p2);
}

interface AnnotatedSub4 extends AnnotatedBase {

    @Override
    public Iterable<@A String> m1(Class<? extends CharSequence> p1, int[] p2);
}

interface AnnotatedSub5 extends AnnotatedBase {

    @Override
    public Iterable<String> m1(@A Class<? extends CharSequence> p1, int[] p2);
}

interface AnnotatedSub6 extends AnnotatedBase {

    @Override
    public Iterable<String> m1(Class<@A ? extends CharSequence> p1, int[] p2);
}

interface AnnotatedSub7 extends AnnotatedBase {

    @Override
    public Iterable<String> m1(Class<? extends @A CharSequence> p1, int[] p2);
}

interface AnnotatedSub8 extends AnnotatedBase {

    @Override
    public Iterable<String> m1(Class<? extends CharSequence> p1, int @A [] p2);
}

@Target({ ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE_USE, ElementType.TYPE_PARAMETER })
@Documented
@interface A {
}
