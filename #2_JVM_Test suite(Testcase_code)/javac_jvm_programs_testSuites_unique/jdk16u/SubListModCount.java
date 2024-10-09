



import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class SubListModCount {
    public static void main(String[] args) {
        int failures = 0;
        var root = new ArrayList<Integer>();
        var subList = root.subList(0, 0);
        root.add(42);
        try {
            subList.size();
            failures++;
            System.out.println("Accessing subList");
        } catch (ConcurrentModificationException expected) {
        }
        var subSubList = subList.subList(0, 0);
        try {
            subSubList.size();
            failures++;
            System.out.println("Accessing subList.subList");
        } catch (ConcurrentModificationException expected) {
        }
        try {
            subSubList.add(42);
            failures++;
            System.out.println("Modifying subList.subList");
        } catch (ConcurrentModificationException expected) {
        }
        try {
            subList.size();
            failures++;
            System.out.println("Accessing subList again");
        } catch (ConcurrentModificationException expected) {
        }
        if (failures > 0) {
            throw new AssertionError(failures + " tests failed");
        }
    }
}
