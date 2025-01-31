

package pkg;

import java.lang.annotation.*;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.util.*;
import java.io.*;



@Target(TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
@interface A {}

@Target(TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@interface DA {}

@Target(TYPE_PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@interface DTPA {}


class T0x00 {
    void m0x00(Long l1) {
        Object l2 = (@A @DA Long) l1;
    }
}


class T0x01<T> {
    void m0x01(List<T> list) {
        List<T> l = (List<@A @DA T>) list;
    }
}


class T0x02 {
    boolean m0x02(String s) {
        return (s instanceof @A @DA String);
    }
}


class T0x03<T> {
    void m0x03(T typeObj, Object obj) {
        boolean ok = obj instanceof String @A @DA [];
    }
}


class T0x04 {
    void m0x04() {
        new @A @DA ArrayList<String>();
    }
}


class T0x05<T> {
    void m0x05() {
        new ArrayList<@A @DA T>();
    }
}


class T0x06 {
    void m0x06(@A @DA T0x06 this) {}
}


class T0x08 {
    void m0x08() {
      @A @DA String s = null;
    }
}


class T0x09<T> {
    void g() {
        List<@A @DA String> l = null;
    }

    void a() {
        String @A @DA [] as = null;
    }
}


class T0x0B {
    Class<@A @DA Object> m0x0B() { return null; }
}


class T0x0D {
    void m0x0D(HashMap<@A @DA Object, List<@A @DA List<@A @DA Class>>> s1) {}
}


class T0x0F {
    HashMap<@A @DA Object, @A @DA Object> c1;
}


class T0x10<T extends @A @DA Cloneable> {
}

class T0x10A<T extends @A @DA Object> {
}


class T0x11<T extends List<@A @DA T>> {
}


class T0x12<T> {
    <T extends @A @DA Cloneable> void m0x12() {}
}


class T0x13 {
    static <T extends Comparable<@A @DA T>> T m0x13() {
        return null;
    }
}


class T0x14 extends @A @DA Thread implements @A @DA Serializable, @A @DA Cloneable {
}


class T0x15<T> extends ArrayList<@A @DA T> {
}


class T0x16 {
    void m0x16() throws @A @DA Exception {}
}


class T0x18<T> {
    <T> T0x18() {}

    void m() {
        new <@A @DA Integer> T0x18();
    }
}


class T0x19 {
    <T> T0x19() {}

    void g() {
       new <List<@A @DA String>> T0x19();
    }
}


class T0x1A<T,U> {
    public static <T, U> T m() { return null; }
    static void m0x1A() {
        T0x1A.<@A @DA Integer, @A @DA Short>m();
    }
}


class T0x1B<T> {
    void m0x1B() {
        Collections.<T @A @DA []>emptyList();
    }
}


class T0x1C {
    void m0x1C(List<? extends @A @DA String> lst) {}
}


class T0x1D<T> {
    void m0x1D(List<? extends @A @DA List<int[]>> lst) {}
}


class T0x20 {
    <@A @DA T> void m0x20() {}
}

class T0x20A {
    <@A @DTPA T> void m0x20A() {}
}

class T0x20B {
    <T> void m0x20B(@A @DA T p) {}
}


class T0x22<@A @DA T> {
}

class T0x22A<@A @DTPA T> {
}

class T0x22B<T> {
    @A @DA T f;
}
