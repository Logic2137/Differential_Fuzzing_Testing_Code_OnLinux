



import java.util.*;
import java.util.function.BiFunction;

public class CheckedMapReplaceAll {
    public static void main(String[] args) {
        Map<Integer,Double> unwrapped = new HashMap<>();
        unwrapped.put(1, 1.0);
        unwrapped.put(2, 2.0);
        unwrapped.put(3, 3.0);

        Map<Integer,Double> wrapped = Collections.checkedMap(unwrapped, Integer.class, Double.class);

        BiFunction evil = (k, v) -> (((int)k) % 2 != 0) ? v : "evil";

        try {
            wrapped.replaceAll(evil);
            System.out.printf("Bwahaha! I have defeated you! %s\n", wrapped);
            throw new RuntimeException("String added to checked Map<Integer,Double>");
        } catch (ClassCastException thwarted) {
            thwarted.printStackTrace(System.out);
            System.out.println("Curses! Foiled again!");
        }
    }
}
