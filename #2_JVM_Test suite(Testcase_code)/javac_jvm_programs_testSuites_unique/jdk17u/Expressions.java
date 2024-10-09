import java.lang.annotation.*;

class Expressions {

    void instanceOf() {
        Object o = null;
        boolean a = o instanceof @A String;
        boolean b = o instanceof @B(0) String;
    }

    void instanceOfArray() {
        Object o = null;
        boolean a1 = o instanceof @A String[];
        boolean a2 = o instanceof @B(0) String[];
        boolean b1 = o instanceof String @A [];
        boolean b2 = o instanceof String @B(0) [];
    }

    void objectCreation() {
        new @A String();
        new @B(0) String();
    }

    void objectCreationArray() {
        Object a1 = new @C String[][] {};
        Object a2 = new @D String[1][];
        Object a3 = new @E String[1][2];
        Object b1 = new @F String @B(1) [][] {};
        Object b2 = new @G String @B(2) [1][];
        Object b3 = new @H String @B(3) [1][2];
        Object c1 = new @I String[] @B(4) [] {};
        Object c2 = new @J String[1] @B(5) [];
        Object c3 = new @K String[1] @B(6) [2];
        Object d1 = new @L String @B(7) [] @B(8) [] {};
        Object d2 = new @M String @B(9) [1] @B(10) [];
        Object d3 = new @N String @B(11) [1] @B(12) [2];
        Object rand = new @O String @B(value = 13) [1] @B(value = 14) [2];
    }
}

@Target({ ElementType.TYPE_USE, ElementType.TYPE_PARAMETER })
@interface A {
}

@Target({ ElementType.TYPE_USE, ElementType.TYPE_PARAMETER })
@interface C {
}

@Target({ ElementType.TYPE_USE, ElementType.TYPE_PARAMETER })
@interface D {
}

@Target({ ElementType.TYPE_USE, ElementType.TYPE_PARAMETER })
@interface E {
}

@Target({ ElementType.TYPE_USE, ElementType.TYPE_PARAMETER })
@interface F {
}

@Target({ ElementType.TYPE_USE, ElementType.TYPE_PARAMETER })
@interface G {
}

@Target({ ElementType.TYPE_USE, ElementType.TYPE_PARAMETER })
@interface H {
}

@Target({ ElementType.TYPE_USE, ElementType.TYPE_PARAMETER })
@interface I {
}

@Target({ ElementType.TYPE_USE, ElementType.TYPE_PARAMETER })
@interface J {
}

@Target({ ElementType.TYPE_USE, ElementType.TYPE_PARAMETER })
@interface K {
}

@Target({ ElementType.TYPE_USE, ElementType.TYPE_PARAMETER })
@interface L {
}

@Target({ ElementType.TYPE_USE, ElementType.TYPE_PARAMETER })
@interface M {
}

@Target({ ElementType.TYPE_USE, ElementType.TYPE_PARAMETER })
@interface N {
}

@Target({ ElementType.TYPE_USE, ElementType.TYPE_PARAMETER })
@interface O {
}

@Target({ ElementType.TYPE_USE, ElementType.TYPE_PARAMETER })
@interface B {

    int value();
}
