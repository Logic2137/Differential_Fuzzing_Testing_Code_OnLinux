
import java.lang.annotation.*;
import static java.lang.annotation.ElementType.*;

import java.util.*;
import java.io.*;



@Target({TYPE_USE, TYPE_PARAMETER, TYPE})
@Retention(RetentionPolicy.RUNTIME)
@interface A {}


class T0x1C {
    void m0x1C(List<? extends @A String> lst) {}
}


class T0x1D<T> {
    void m0x1D(List<? extends @A List<int[]>> lst) {}
}


class T0x00 {
    void m0x00(Long l1) {
        Object l2 = (@A Long) l1;
    }
}


class T0x01<T> {
    void m0x01(List<T> list) {
        List<T> l = (List<@A T>) list;
    }
}


class T0x02 {
    boolean m0x02(String s) {
        return (s instanceof @A String);
    }
}


class T0x04 {
    void m0x04() {
        new @A ArrayList<String>();
    }
}


class T0x08 {
    void m0x08() {
      @A String s = null;
    }
}


class T0x0D {
    void m0x0D(HashMap<@A Object, List<@A List<@A Class>>> s1) {}
}


class T0x06 {
    void m0x06(@A T0x06 this) {}
}


class T0x0B {
    Class<@A Object> m0x0B() { return null; }
}


class T0x0F {
    HashMap<@A Object, @A Object> c1;
}


class T0x20<T, U> {
    <@A T, @A U> void m0x20() {}
}


class T0x22<@A T, @A U> {
}


class T0x10<T extends @A Object> {
}


class T0x12<T> {
    <T extends @A Object> void m0x12() {}
}


class T0x11<T extends List<@A T>> {
}



class T0x13 {
    static <T extends Comparable<@A T>> T m0x13() {
        return null;
    }
}


class T0x15<T> extends ArrayList<@A T> {
}


class T0x03<T> {
    void m0x03(T typeObj, Object obj) {
        boolean ok = obj instanceof String @A [];
    }
}


class T0x05<T> {
    void m0x05() {
        new ArrayList<@A T>();
    }
}


class T0x09<T> {
    void g() {
        List<@A String> l = null;
    }

    void a() {
        String @A [] as = null;
    }
}


class T0x19 {
    <T> T0x19() {}

    void g() {
       new <List<@A String>> T0x19();
    }
}


class T0x1B<T> {
    void m0x1B() {
        Collections.<T @A []>emptyList();
    }
}


class T0x18<T> {
    <T> T0x18() {}

    void m() {
        new <@A Integer> T0x18();
    }
}


class T0x1A<T,U> {
    public static <T, U> T m() { return null; }
    static void m0x1A() {
        T0x1A.<@A Integer, @A Short>m();
    }
}


class T0x14 extends @A Object implements @A Serializable, @A Cloneable {
}


class T0x16 {
    void m0x16() throws @A Exception {}
}


class ResourceVariables {
    void m() throws Exception {
        try (@A InputStream is = new @A FileInputStream("x")){}
    }
}


class ExceptionParameters {
    void multipleExceptions() {
        try {
            new Object();
        } catch (@A Exception e) {}
        try {
            new Object();
        } catch (@A Exception e) {}
        try {
            new Object();
        } catch (@A Exception e) {}
    }
}
