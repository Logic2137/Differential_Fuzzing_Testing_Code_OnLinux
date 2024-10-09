



import java.util.*;

import java.security.*;

public class Nulls {

    public static void main(String[] args) throws Exception {
        try {
            Security.addProvider(null);
            throw new Exception();
        } catch (NullPointerException e) {
            System.out.println("addProvider(null): " + e);
        }
        if (Security.getAlgorithms(null).isEmpty() == false) {
            throw new Exception();
        }
        try {
            Security.getProperty(null);
            throw new Exception();
        } catch (NullPointerException e) {
            System.out.println("getProperty(null): " + e);
        }
        if (Security.getProvider(null) != null) {
            throw new Exception();
        }
        try {
            Security.getProviders((Map)null);
            throw new Exception();
        } catch (NullPointerException e) {
            System.out.println("getProviders((Map)null): " + e);
        }
        try {
            Security.getProviders((String)null);
            throw new Exception();
        } catch (NullPointerException e) {
            System.out.println("getProviders((String)null): " + e);
        }
        try {
            Security.insertProviderAt(null, 1);
            throw new Exception();
        } catch (NullPointerException e) {
            System.out.println("insertProviderAt(null): " + e);
        }
        Security.removeProvider(null);
        try {
            Security.setProperty("foo", null);
            throw new Exception();
        } catch (NullPointerException e) {
            System.out.println("setProperty(\"foo\", null): " + e);
        }
        try {
            Security.setProperty(null, "foo");
            throw new Exception();
        } catch (NullPointerException e) {
            System.out.println("setProperty(null, \"foo\"): " + e);
        }
        try {
            Security.setProperty(null, null);
            throw new Exception();
        } catch (NullPointerException e) {
            System.out.println("setProperty(null, null): " + e);
        }
        if (Security.getAlgorithmProperty(null, null) != null) {
            throw new Exception();
        }
    }

}
