import java.net.*;

public class UseSTD3ASCIIRules {

    public static void main(String[] args) throws Exception {
        String[] illegalNames = { "www.example.com-", "-www.example.com", "-www.example.com-", "www.ex\u002Cmple.com", "www.ex\u007Bmple.com", "www.ex\u007Fmple.com" };
        String[] legalNames = { "www.ex-ample.com", "www.ex\u002Dmple.com", "www.ex\u007Ample.com", "www.ex\u3042mple.com", "www.\u3042\u3044\u3046.com", "www.\u793A\u4F8B.com" };
        for (String name : illegalNames) {
            try {
                System.out.println("Convering illegal IDN: " + name);
                IDN.toASCII(name, IDN.USE_STD3_ASCII_RULES);
                throw new Exception("Expected to get IllegalArgumentException for " + name);
            } catch (IllegalArgumentException iae) {
            }
        }
        for (String name : legalNames) {
            System.out.println("Convering legal IDN: " + name);
            System.out.println("\tThe ACE form is: " + IDN.toASCII(name, IDN.USE_STD3_ASCII_RULES));
        }
    }
}
