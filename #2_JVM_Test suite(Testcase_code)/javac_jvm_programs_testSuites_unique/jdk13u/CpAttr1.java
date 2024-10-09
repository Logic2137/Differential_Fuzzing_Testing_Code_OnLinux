public class CpAttr1 {

    public static void main(String[] args) {
        
        System.out.println("2");
        CpAttr2.doit();
        
        System.out.println("3");
        CpAttr3.doit();
        
        System.out.println("4");
        CpAttr4.doit();
        
        System.out.println("5");
        CpAttr5.doit();
        System.out.println("Test passed");
    }
}

class CpAttr2 {

    static void doit() {
        throw new RuntimeException("");
    }
}

class CpAttr3 {

    static void doit() {
        throw new RuntimeException("");
    }
}

class CpAttr4 {

    static void doit() {
        throw new RuntimeException("");
    }
}

class CpAttr5 {

    static void doit() {
        throw new RuntimeException("");
    }
}
