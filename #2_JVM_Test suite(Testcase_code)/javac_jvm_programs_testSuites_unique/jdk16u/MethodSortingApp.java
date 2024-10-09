

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class MethodSortingApp {
    static class HelloA {
        String aaaa() { return "aaaa"; }
        String bbbb() { return "bbbb"; }
        String dddd() { return "dddd"; }
        String eeee() { return "eeee"; }
        String gggg() { return "gggg"; }
    }

    static class HelloA1 extends HelloA {
        String aaaa() { return "aaa-"; }
        String dddd() { return "ddd-"; }
        String gggg() { return "ggg-"; }
    }

    static class HelloB {
        String aaaa() { return "aaaa"; }
        String cccc() { return "cccc"; }
        String dddd() { return "dddd"; }
        String ffff() { return "ffff"; }
        String gggg() { return "gggg"; }
    }

    static class HelloB1 extends HelloB {
        String aaaa() { return "aaa-"; }
        String dddd() { return "ddd-"; }
        String gggg() { return "ggg-"; }
    }

    
    static interface InterfaceA {
        default public String aaa() { return "aaa";}
        default public String bbb() { return "bbb";}
        default public String ddd() { return "ddd";}
        default public String eee() { return "eee";}
        default public String ggg() { return "ggg";}
    }

    static class ImplementorA implements InterfaceA {
        public String aaa() { return "aa-";}
    }

    static class ImplementorA1 extends ImplementorA {
        public String bbb() { return "bb-";}
    }

    static interface InterfaceB {
        default public String aaa() { return "aaa"; }
        default public String ccc() { return "ccc"; }
        default public String ddd() { return "ddd"; }
        default public String fff() { return "fff"; }
        default public String ggg() { return "ggg"; }
    }

    static class ImplementorB implements InterfaceB {
        public String ggg() { return "gg-";}
    }

    static class ImplementorB1 extends ImplementorB {
        public String fff() { return "ff-";}
    }


    public static void main(String args[]) {
        testSimpleMethods();
        testDefaultMethods();
        testMixedInterfaces();
    }

    static void testSimpleMethods() {
        
        
        
        
        HelloA1 a1 = new HelloA1();
        HelloA a = new HelloA();
        assertEqual(a.aaaa(), "aaaa");
        assertEqual(a.bbbb(), "bbbb");
        assertEqual(a.dddd(), "dddd");
        assertEqual(a.eeee(), "eeee");
        assertEqual(a.gggg(), "gggg");

        assertEqual(a1.aaaa(), "aaa-");
        assertEqual(a1.bbbb(), "bbbb");
        assertEqual(a1.dddd(), "ddd-");
        assertEqual(a1.eeee(), "eeee");
        assertEqual(a1.gggg(), "ggg-");

        HelloB b = new HelloB();
        assertEqual(b.aaaa(), "aaaa");
        assertEqual(b.cccc(), "cccc");
        assertEqual(b.dddd(), "dddd");
        assertEqual(b.ffff(), "ffff");
        assertEqual(b.gggg(), "gggg");

        HelloB b1 = new HelloB1();
        assertEqual(b1.aaaa(), "aaa-");
        assertEqual(b1.cccc(), "cccc");
        assertEqual(b1.dddd(), "ddd-");
        assertEqual(b1.ffff(), "ffff");
        assertEqual(b1.gggg(), "ggg-");
    }

    static void testDefaultMethods() {
        InterfaceA a1 = new ImplementorA1();
        InterfaceA a = new ImplementorA();

        assertEqual(a.aaa(), "aa-");
        assertEqual(a.bbb(), "bbb");
        assertEqual(a.ddd(), "ddd");
        assertEqual(a.eee(), "eee");
        assertEqual(a.ggg(), "ggg");

        assertEqual(a1.aaa(), "aa-");
        assertEqual(a1.bbb(), "bb-");
        assertEqual(a1.ddd(), "ddd");
        assertEqual(a1.eee(), "eee");
        assertEqual(a1.ggg(), "ggg");

        InterfaceB b = new ImplementorB();
        InterfaceB b1 = new ImplementorB1();

        assertEqual(b.aaa(), "aaa");
        assertEqual(b.ccc(), "ccc");
        assertEqual(b.ddd(), "ddd");
        assertEqual(b.fff(), "fff");
        assertEqual(b.ggg(), "gg-");

        assertEqual(b1.aaa(), "aaa");
        assertEqual(b1.ccc(), "ccc");
        assertEqual(b1.ddd(), "ddd");
        assertEqual(b1.fff(), "ff-");
        assertEqual(b1.ggg(), "gg-");
    }

    
    
    static void testMixedInterfaces() {
        Object xx = new SynchronousQueue();
        BlockingQueue yy = (BlockingQueue)xx;
    }

    static private void assertEqual(String a, String b) {
        if (!a.equals(b)) {
            throw new RuntimeException(a + " is not equal to " + b);
        } else {
            System.out.println("Expected: " + a + ", got: " + b);
        }
    }
}
