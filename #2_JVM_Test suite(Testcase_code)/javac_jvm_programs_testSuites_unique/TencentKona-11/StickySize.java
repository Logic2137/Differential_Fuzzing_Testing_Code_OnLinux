



import java.io.*;
import java.util.*;

public class StickySize {
    static void equalClones(BitSet s, int expectedSize) {
        equal(expectedSize, clone(s).size());
        equal(expectedSize, serialClone(s).size());
        equal(expectedSize, s.size());
        equal(clone(s), serialClone(s));
    }

    private static void realMain(String[] args) {
        BitSet s;

        s = new BitSet();       
        equal(s.size(), 64);
        equalClones(s, 0);
        s.set(3*64);
        s.set(7*64);
        equal(s.size(), 8*64);
        equalClones(s, 8*64);
        s.clear(7*64);
        equal(s.size(), 8*64);
        equalClones(s, 4*64);

        s = new BitSet(8*64);   
        equalClones(s, 8*64);
        s.set(3*64);
        s.set(7*64);
        equalClones(s, 8*64);
        s.clear(7*64);
        equalClones(s, 8*64);
        equalClones(clone(s), 8*64);
        equalClones(serialClone(s), 8*64);
        s.set(17*64);           
        equalClones(s, 18*64);
        s.clear(17*64);
        equalClones(s, 4*64);
    }

    static BitSet clone(BitSet s) {
        return (BitSet) s.clone();
    }

    
    static volatile int passed = 0, failed = 0;
    static void pass() {passed++;}
    static void fail() {failed++; Thread.dumpStack();}
    static void fail(String msg) {System.out.println(msg); fail();}
    static void unexpected(Throwable t) {failed++; t.printStackTrace();}
    static void check(boolean cond) {if (cond) pass(); else fail();}
    static void equal(Object x, Object y) {
        if (x == null ? y == null : x.equals(y)) pass();
        else fail(x + " not equal to " + y);}
    public static void main(String[] args) throws Throwable {
        try {realMain(args);} catch (Throwable t) {unexpected(t);}
        System.out.printf("%nPassed = %d, failed = %d%n%n", passed, failed);
        if (failed > 0) throw new AssertionError("Some tests failed");}
    static byte[] serializedForm(Object obj) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            new ObjectOutputStream(baos).writeObject(obj);
            return baos.toByteArray();
        } catch (IOException e) { throw new RuntimeException(e); }}
    static Object readObject(byte[] bytes)
        throws IOException, ClassNotFoundException {
        InputStream is = new ByteArrayInputStream(bytes);
        return new ObjectInputStream(is).readObject();}
    @SuppressWarnings("unchecked")
    static <T> T serialClone(T obj) {
        try { return (T) readObject(serializedForm(obj)); }
        catch (Exception e) { throw new RuntimeException(e); }}
}
