import java.util.*;
import java.util.function.UnaryOperator;

public class CheckedListReplaceAll {

    public static void main(String[] args) {
        List unwrapped = Arrays.asList(new Object[] { 1, 2, 3 });
        List<Object> wrapped = Collections.checkedList(unwrapped, Integer.class);
        UnaryOperator evil = e -> (((int) e) % 2 != 0) ? e : "evil";
        try {
            wrapped.replaceAll(evil);
            System.out.printf("Bwahaha! I have defeated you! %s\n", wrapped);
            throw new RuntimeException("String added to checked List<Integer>");
        } catch (ClassCastException thwarted) {
            thwarted.printStackTrace(System.out);
            System.out.println("Curses! Foiled again!");
        }
        unwrapped = Arrays.asList(new Object[] {});
        wrapped = Collections.checkedList(unwrapped, Integer.class);
        try {
            wrapped.replaceAll((UnaryOperator) null);
            System.out.printf("Bwahaha! I have defeated you! %s\n", wrapped);
            throw new RuntimeException("NPE not thrown when passed a null operator");
        } catch (NullPointerException thwarted) {
            thwarted.printStackTrace(System.out);
            System.out.println("Curses! Foiled again!");
        }
    }
}
