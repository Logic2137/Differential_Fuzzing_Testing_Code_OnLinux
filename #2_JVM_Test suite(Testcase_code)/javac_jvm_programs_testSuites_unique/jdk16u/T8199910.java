


import java.util.List;

class T8199910 {
    <T> T first(T... ts) {
        return ts[0];
    }

    void m() {
        var list1 = List.of("", 1);
        var list2 = List.of(1, 2.0);
        var a = first("", 1);
        var b = first(1, 2.0);
    }
}
