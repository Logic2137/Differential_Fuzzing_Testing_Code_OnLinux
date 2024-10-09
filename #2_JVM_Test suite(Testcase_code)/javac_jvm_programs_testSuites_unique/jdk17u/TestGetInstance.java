import java.security.*;
import javax.crypto.*;

public class TestGetInstance {

    private static void same(Object o1, Object o2) throws Exception {
        if (o1 != o2) {
            throw new Exception("not same object");
        }
    }

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        Provider p = Security.getProvider("SunJCE");
        KeyGenerator kg;
        kg = KeyGenerator.getInstance("des");
        System.out.println("Default: " + kg.getProvider().getName());
        kg = KeyGenerator.getInstance("des", "SunJCE");
        same(p, kg.getProvider());
        kg = KeyGenerator.getInstance("des", p);
        same(p, kg.getProvider());
        try {
            kg = KeyGenerator.getInstance("foo");
            throw new AssertionError();
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        try {
            kg = KeyGenerator.getInstance("foo", "SunJCE");
            throw new AssertionError();
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        try {
            kg = KeyGenerator.getInstance("foo", p);
            throw new AssertionError();
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        try {
            kg = KeyGenerator.getInstance("foo", "SUN");
            throw new AssertionError();
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        try {
            kg = KeyGenerator.getInstance("foo", Security.getProvider("SUN"));
            throw new AssertionError();
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        try {
            kg = KeyGenerator.getInstance("foo", "foo");
            throw new AssertionError();
        } catch (NoSuchProviderException e) {
            System.out.println(e);
        }
        long stop = System.currentTimeMillis();
        System.out.println("Done (" + (stop - start) + " ms).");
    }
}
