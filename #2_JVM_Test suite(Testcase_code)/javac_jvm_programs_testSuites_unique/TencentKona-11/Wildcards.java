

package typeannos;

import java.lang.annotation.*;


class BoundTest {
    void wcExtends(MyList<? extends @WldA String> l) { }
    void wcSuper(MyList<? super @WldA String> l) { }

    MyList<? extends @WldA String> returnWcExtends() { return null; }
    MyList<? super @WldA String> returnWcSuper() { return null; }
    MyList<? extends @WldA MyList<? super @WldB("m") String>> complex() { return null; }
}

class BoundWithValue {
    void wcExtends(MyList<? extends @WldB("m") String> l) { }
    void wcSuper(MyList<? super @WldB("m") String> l) { }

    MyList<? extends @WldB("m") String> returnWcExtends() { return null; }
    MyList<? super @WldB("m") String> returnWcSuper() { return null; }
    MyList<? extends @WldB("m") MyList<? super @WldB("m") String>> complex() { return null; }
}

class SelfTest {
    void wcExtends(MyList<@WldA ?> l) { }
    void wcSuper(MyList<@WldA ?> l) { }

    MyList<@WldA ?> returnWcExtends() { return null; }
    MyList<@WldA ?> returnWcSuper() { return null; }
    MyList<@WldA ? extends @WldA MyList<@WldB("m") ?>> complex() { return null; }
}

class SelfWithValue {
    void wcExtends(MyList<@WldB("m") ?> l) { }
    void wcSuper(MyList<@WldB("m") ?> l) { }

    MyList<@WldB("m") ?> returnWcExtends() { return null; }
    MyList<@WldB("m") ?> returnWcSuper() { return null; }
    MyList<@WldB("m") ? extends MyList<@WldB("m") ? super String>> complex() { return null; }
}

class MyList<K> { }

@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
@Documented
@interface WldA { }
@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
@Documented
@interface WldB { String value(); }
