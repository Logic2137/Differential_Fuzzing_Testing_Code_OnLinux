import java.util.Collections;
import java.util.List;

class T5078378 {

    public static boolean contains(List l, Object o) {
        return Collections.binarySearch(l, o) > -1;
    }
}
