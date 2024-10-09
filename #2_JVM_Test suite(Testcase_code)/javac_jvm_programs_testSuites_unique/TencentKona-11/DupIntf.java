



interface SAM<P1> {
    P1 m();
}

interface Other { }

public class DupIntf {
    public static void main(String argv[]) {
        SAM<?> sam = (SAM<?> & Other) () -> "Pass.";
        System.out.println(sam.m());
    }
}
