

import java.util.List;


class CrashInAnnotateTest {
}


class CrashInAnnotateTest2 {
    void compare(Object o, List<List<Object>> l) {}
}


class CrashInAnnotateTest3 { }


class CrashInAnnotateTest4 {
    class Inner {
        Object aField;
    }
}
