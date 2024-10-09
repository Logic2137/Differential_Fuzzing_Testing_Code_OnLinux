import java.util.Comparator;
import java.io.Serializable;

public final class Item extends Number implements Comparable<Item>, Serializable {

    public final int value;

    public Item(int v) {
        value = v;
    }

    public Item(Item i) {
        value = i.value;
    }

    public Item(Integer i) {
        value = i.intValue();
    }

    public static Item valueOf(int i) {
        return new Item(i);
    }

    public int intValue() {
        return value;
    }

    public long longValue() {
        return (long) value;
    }

    public float floatValue() {
        return (float) value;
    }

    public double doubleValue() {
        return (double) value;
    }

    public boolean equals(Object x) {
        return (x instanceof Item) && ((Item) x).value == value;
    }

    public boolean equals(int b) {
        return value == b;
    }

    public int compareTo(Item x) {
        return Integer.compare(this.value, x.value);
    }

    public int compareTo(int b) {
        return Integer.compare(this.value, b);
    }

    public int hashCode() {
        return value;
    }

    public String toString() {
        return Integer.toString(value);
    }

    public static int compare(Item x, Item y) {
        return Integer.compare(x.value, y.value);
    }

    public static int compare(Item x, int b) {
        return Integer.compare(x.value, b);
    }

    public static Comparator<Item> comparator() {
        return new Cpr();
    }

    public static class Cpr implements Comparator<Item> {

        public int compare(Item x, Item y) {
            return Integer.compare(x.value, y.value);
        }
    }
}
