public class Bug8148174 {

    public static void main(String[] args) {
        int size = Integer.MAX_VALUE - 2;
        java.util.Vector<Object> huge = new java.util.Vector<>(size);
        for (int i = 0; i < size; i++) huge.add(null);
        try {
            huge.addAll(huge);
            throw new Error("expected OutOfMemoryError not thrown");
        } catch (OutOfMemoryError success) {
        }
    }
}
