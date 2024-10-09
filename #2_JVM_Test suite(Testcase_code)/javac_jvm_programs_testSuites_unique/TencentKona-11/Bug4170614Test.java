




package java.text;
import sun.text.IntHashtable;



public class Bug4170614Test {
    public static void main(String[] args) throws Exception {
        testIntHashtable();
        testPatternEntry();
    }


    public static void testIntHashtable() throws Exception {
        IntHashtable fred = new IntHashtable();
        fred.put(1, 10);
        fred.put(2, 20);
        fred.put(3, 30);

        IntHashtable barney = new IntHashtable();
        barney.put(1, 10);
        barney.put(3, 30);
        barney.put(2, 20);

        IntHashtable homer = new IntHashtable();
        homer.put(3, 30);
        homer.put(1, 10);
        homer.put(7, 900);

        if (fred.equals(barney)) {
            System.out.println("fred.equals(barney)");
        }
        else {
            System.out.println("!fred.equals(barney)");
        }
        System.out.println("fred.hashCode() == " + fred.hashCode());
        System.out.println("barney.hashCode() == " + barney.hashCode());

        if (!fred.equals(barney)) {
            throw new Exception("equals() failed on two hashtables that are equal");
        }

        if (fred.hashCode() != barney.hashCode()) {
           throw new Exception("hashCode() failed on two hashtables that are equal");
        }

        System.out.println();
        if (fred.equals(homer)) {
            System.out.println("fred.equals(homer)");
        }
        else {
            System.out.println("!fred.equals(homer)");
        }
        System.out.println("fred.hashCode() == " + fred.hashCode());
        System.out.println("homer.hashCode() == " + homer.hashCode());

        if (fred.equals(homer)) {
            throw new Exception("equals() failed on two hashtables that are not equal");
        }

        if (fred.hashCode() == homer.hashCode()) {
            throw new Exception("hashCode() failed on two hashtables that are not equal");
        }

        System.out.println();
        System.out.println("testIntHashtable() passed.\n");
    }

    public static void testPatternEntry() throws Exception {
        PatternEntry fred = new PatternEntry(1,
                                             new StringBuffer("hello"),
                                             new StringBuffer("up"));
        PatternEntry barney = new PatternEntry(1,
                                               new StringBuffer("hello"),
                                               new StringBuffer("down"));
        
        PatternEntry homer = new PatternEntry(1,
                                              new StringBuffer("goodbye"),
                                              new StringBuffer("up"));

        if (fred.equals(barney)) {
            System.out.println("fred.equals(barney)");
        }
        else {
            System.out.println("!fred.equals(barney)");
        }
        System.out.println("fred.hashCode() == " + fred.hashCode());
        System.out.println("barney.hashCode() == " + barney.hashCode());

        if (!fred.equals(barney)) {
            throw new Exception("equals() failed on two hashtables that are equal");
        }

        if (fred.hashCode() != barney.hashCode()) {
           throw new Exception("hashCode() failed on two hashtables that are equal");
        }

        System.out.println();
        if (fred.equals(homer)) {
            System.out.println("fred.equals(homer)");
        }
        else {
            System.out.println("!fred.equals(homer)");
        }
        System.out.println("fred.hashCode() == " + fred.hashCode());
        System.out.println("homer.hashCode() == " + homer.hashCode());

        if (fred.equals(homer)) {
            throw new Exception("equals() failed on two hashtables that are not equal");
        }

        if (fred.hashCode() == homer.hashCode()) {
            throw new Exception("hashCode() failed on two hashtables that are not equal");
        }

        System.out.println();
        System.out.println("testPatternEntry() passed.\n");
    }
}
