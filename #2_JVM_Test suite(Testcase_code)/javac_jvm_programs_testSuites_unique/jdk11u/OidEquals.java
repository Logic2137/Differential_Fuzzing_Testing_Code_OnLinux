



import sun.security.util.ObjectIdentifier;

public class OidEquals {
    public static void main(String[] args) throws Exception {
        ObjectIdentifier oid1 = new ObjectIdentifier("1.3.6.1.4.1.42.2.17");
        ObjectIdentifier oid2 =
                new ObjectIdentifier(new int[]{1, 3, 6, 1, 4, 1, 42, 2, 17});
        ObjectIdentifier oid3 = new ObjectIdentifier("1.2.3.4");

        assertEquals(oid1, oid1);
        assertEquals(oid1, oid2);
        assertNotEquals(oid1, oid3);
        assertNotEquals(oid1, "1.3.6.1.4.1.42.2.17");

        System.out.println("Tests passed.");
    }

    static void assertEquals(ObjectIdentifier oid, Object obj)
            throws Exception {
        if (!oid.equals(obj)) {
            throw new Exception("The ObjectIdentifier " + oid.toString() +
                    " should be equal to the Object " + obj.toString());
        }
    }

    static void assertNotEquals(ObjectIdentifier oid, Object obj)
            throws Exception {
        if (oid.equals(obj)) {
            throw new Exception("The ObjectIdentifier " + oid.toString() +
                    " should not be equal to the Object " + obj.toString());
        }
    }
}
