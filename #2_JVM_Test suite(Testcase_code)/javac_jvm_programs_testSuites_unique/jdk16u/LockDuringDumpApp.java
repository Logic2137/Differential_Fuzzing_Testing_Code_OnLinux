

public class LockDuringDumpApp {
    static String LITERAL = "@@LockDuringDump@@LITERAL"; 

    public static void main(String args[]) {
        synchronized (LITERAL) { 
            System.out.println("I am able to lock the literal string \"" + LITERAL + "\"");
        }
    }
}
