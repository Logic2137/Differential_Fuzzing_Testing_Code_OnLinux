


public class TestEscapeThroughInvoke {
    private A a;

    public static void main(String[] args) {
        TestEscapeThroughInvoke test = new TestEscapeThroughInvoke();
        test.a = new A(42);
        
        for (int i = 0; i < 100_000; ++i) {
            test.run();
        }
    }

    private void run() {
        
        new Object();
        
        
        A escapingA = create(42);
        a.check(escapingA);
    }

    
    
    private A create(Integer dummy) {
        A result = new A(dummy);
        result.saveInto(a, dummy); 
        return result;
    }
}

class A {
    private A saved;

    public A(Integer dummy) { }

    public void saveInto(A other, Integer dummy) {
        other.saved = this;
    }

    public void check(A other) {
        if (this.saved != other) {
            throw new RuntimeException("TEST FAILED: Objects not equal.");
        }
    }
}
