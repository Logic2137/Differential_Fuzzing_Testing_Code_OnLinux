import sun.security.krb5.PrincipalName;

public class Immutable {

    public static void main(String[] args) throws Exception {
        PrincipalName pn1 = new PrincipalName("host/service@REALM");
        PrincipalName pn2 = (PrincipalName) pn1.clone();
        pn1.getNameStrings()[0] = "http";
        if (!pn1.equals(pn2)) {
            throw new Exception();
        }
    }
}
