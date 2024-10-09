public class NativeInstanceFilterTarg {

    public static void main(String[] args) {
        String s1 = "abc";
        String s2 = "def";
        latch(s1);
        s1.intern();
        s2.intern();
    }

    public static String latch(String s) {
        return s;
    }
}
