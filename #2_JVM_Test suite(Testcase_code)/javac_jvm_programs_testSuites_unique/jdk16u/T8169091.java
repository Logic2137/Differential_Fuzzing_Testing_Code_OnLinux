

import java.io.Serializable;
import java.util.Comparator;

interface T8169091 {
    static <T extends Comparable<? super T>> Comparator<T> comparator() {
        return (Comparator<T> & Serializable)T::compareTo;
    }
}
