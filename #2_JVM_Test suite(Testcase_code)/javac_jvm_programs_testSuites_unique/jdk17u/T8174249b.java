import java.util.*;

class T8174249b {

    static void cs(Collection<String> cs) {
    }

    void test1(Collection c) {
        cs(rawCollection((Class) null));
        Collection<String> cs1 = rawCollection((Class) null);
    }

    void test2(Collection c) {
        cs(rawCollection2((Class) null));
        Collection<String> cs2 = rawCollection2((Class) null);
    }

    void test3(Collection c) {
        cs(rawCollection3((Class) null));
        Collection<String> cs3 = rawCollection2((Class) null);
    }

    Collection<Integer> rawCollection(Class<String> cs) {
        return null;
    }

    <Z> Collection<Integer> rawCollection2(Class<Z> cs) {
        return null;
    }

    <Z> Collection<Z> rawCollection3(Class<Z> cs) {
        return null;
    }
}
