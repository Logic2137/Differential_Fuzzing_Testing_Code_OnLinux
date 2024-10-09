



public class TestConstructorHierarchy {

    static class NestedA extends ExternalSuper {
        private NestedA() {}
        protected NestedA(int i) {} 
    }

    
    
    
    
    

    public static void main(String[] args) throws Throwable {
        try {
            new ExternalSuper();
            throw new Error("Unexpected construction of ExternalSuper");
        }
        catch (IllegalAccessError iae) {
            if (iae.getMessage().contains("class TestConstructorHierarchy tried to access private method 'void ExternalSuper.<init>()'")) {
                System.out.println("Got expected exception constructing ExternalSuper: " + iae);
            }
            else throw new Error("Unexpected IllegalAccessError: " + iae);
        }
        try {
            new NestedA();
            throw new Error("Unexpected construction of NestedA and supers");
        }
        catch (IllegalAccessError iae) {
            if (iae.getMessage().contains("class TestConstructorHierarchy$NestedA tried to access private method 'void ExternalSuper.<init>()'")) {
                System.out.println("Got expected exception constructing NestedA: " + iae);
            }
            else throw new Error("Unexpected IllegalAccessError: " + iae);
        }
        try {
            new ExternalSub();
            throw new Error("Unexpected construction of ExternalSub");
        }
        catch (IllegalAccessError iae) {
            if (iae.getMessage().contains("class ExternalSub tried to access private method 'void TestConstructorHierarchy$NestedA.<init>()'")) {
                System.out.println("Got expected exception constructing ExternalSub: " + iae);
            }
            else throw new Error("Unexpected IllegalAccessError: " + iae);
        }
    }
}






class ExternalSuper {
    public ExternalSuper() { }
}


class ExternalSub extends TestConstructorHierarchy.NestedA {
    public ExternalSub() {
        super(0); 
    }
}
