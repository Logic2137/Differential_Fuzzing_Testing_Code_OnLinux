

import java.util.*;

abstract class T8176534 {
    List<String> f(Enumeration e) {
        return newArrayList(forEnumeration(e));
    }

    abstract <T> Iterator<T> forEnumeration(Enumeration<T> e);
    abstract <E> ArrayList<E> newArrayList(Iterator<? extends E> xs);
    abstract <E> ArrayList<E> newArrayList(Iterable<? extends E> xs);
}
