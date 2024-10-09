import sun.security.krb5.internal.KerberosTime;

public class MicroTime {

    public static void main(String[] args) throws Exception {
        KerberosTime t1 = KerberosTime.now();
        KerberosTime last = t1;
        int count = 0;
        while (true) {
            KerberosTime t2 = KerberosTime.now();
            if (t2.getTime() - t1.getTime() > 1000)
                break;
            if (!last.equals(t2)) {
                last = t2;
                count++;
            }
        }
        if (count < 10000) {
            throw new Exception("What? only " + (1000000 / count) + " musec precision?");
        }
    }
}
