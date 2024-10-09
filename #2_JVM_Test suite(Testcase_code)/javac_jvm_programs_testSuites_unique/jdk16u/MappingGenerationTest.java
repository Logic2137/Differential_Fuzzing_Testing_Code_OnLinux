

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.SystemFlavorMap;
import java.util.List;



public class MappingGenerationTest {

    private static final SystemFlavorMap fm =
        (SystemFlavorMap)SystemFlavorMap.getDefaultFlavorMap();

    public static void main(String[] args)  {
        test1();
        test2();
        test3();
        test4();
        test5();
        test6();
    }

    
    public static void test1() {
        DataFlavor df = new DataFlavor("text/plain-test1", null);
        String nat = "native1";

        List<String> natives = fm.getNativesForFlavor(df);
        fm.addUnencodedNativeForFlavor(df, nat);
        List<String> nativesNew = fm.getNativesForFlavor(df);
        if (natives.equals(nativesNew)) {
            System.err.println("orig=" + natives);
            System.err.println("new=" + nativesNew);
            throw new RuntimeException("Test failed");
        }

        List<DataFlavor> flavors = fm.getFlavorsForNative(nat);
        fm.addFlavorForUnencodedNative(nat, df);
        List<DataFlavor> flavorsNew = fm.getFlavorsForNative(nat);
        if (flavors.equals(flavorsNew)) {
            System.err.println("orig=" + flavors);
            System.err.println("new=" + flavorsNew);
            throw new RuntimeException("Test failed");
        }
    }

    
    public static void test2() {
        DataFlavor df = new DataFlavor("text/plain-test2", null);
        String nat = "native2";
        DataFlavor extraDf = new DataFlavor("text/test", null);

        List<String> natives = fm.getNativesForFlavor(df);
        natives.add("Should not be here");
        java.util.List nativesNew = fm.getNativesForFlavor(df);
        if (natives.equals(nativesNew)) {
            System.err.println("orig=" + natives);
            System.err.println("new=" + nativesNew);
            throw new RuntimeException("Test failed");
        }

        List<DataFlavor> flavors = fm.getFlavorsForNative(nat);
        flavors.add(extraDf);
        java.util.List flavorsNew = fm.getFlavorsForNative(nat);
        if (flavors.equals(flavorsNew)) {
            System.err.println("orig=" + flavors);
            System.err.println("new=" + flavorsNew);
            throw new RuntimeException("Test failed");
        }
    }

    
    public static void test3() {
        DataFlavor df1 = new DataFlavor("text/plain-test3", null);
        DataFlavor df2 = new DataFlavor("text/plain-test3; charset=Unicode; class=java.io.Reader", null);
        String nat = "native3";
        List<String> natives = fm.getNativesForFlavor(df2);
        fm.addUnencodedNativeForFlavor(df1, nat);
        List<String> nativesNew = fm.getNativesForFlavor(df2);
        if (!natives.equals(nativesNew)) {
            System.err.println("orig=" + natives);
            System.err.println("new=" + nativesNew);
            throw new RuntimeException("Test failed");
        }
    }

    
    public static void test4() {
        DataFlavor df = new DataFlavor("text/plain-test4; charset=Unicode; class=java.io.Reader", null);
        String nat = "native4";
        List<String> natives = fm.getNativesForFlavor(df);
        if (!natives.contains(nat)) {
            fm.addUnencodedNativeForFlavor(df, nat);
            List<String> nativesNew = fm.getNativesForFlavor(df);
            natives.add(nat);
            if (!natives.equals(nativesNew)) {
                System.err.println("orig=" + natives);
                System.err.println("new=" + nativesNew);
                throw new RuntimeException("Test failed");
            }
        }
    }

    
    public static void test5() {
        final DataFlavor flavor =
            new DataFlavor("text/plain-TEST5; charset=Unicode", null);

        fm.getNativesForFlavor(flavor);

        fm.setNativesForFlavor(flavor, new String[0]);

        List<String> natives = fm.getNativesForFlavor(flavor);

        if (!natives.isEmpty()) {
            System.err.println("natives=" + natives);
            throw new RuntimeException("Test failed");
        }
    }

    
    public static void test6() {
        final String nat = "STRING";
        fm.getFlavorsForNative(nat);
        fm.setFlavorsForNative(nat, new DataFlavor[0]);

        List<DataFlavor> flavors = fm.getFlavorsForNative(nat);

        if (!flavors.isEmpty()) {
            System.err.println("flavors=" + flavors);
            throw new RuntimeException("Test failed");
        }
    }
}
