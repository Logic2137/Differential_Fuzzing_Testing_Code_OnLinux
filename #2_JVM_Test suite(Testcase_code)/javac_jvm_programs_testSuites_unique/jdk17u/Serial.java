import javax.security.auth.*;
import java.io.*;
import java.util.*;

public class Serial implements java.io.Serializable {

    public static void main(String[] args) {
        try {
            FileOutputStream fos = new FileOutputStream("serial.tmp");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            PrivateCredentialPermission pcp = new PrivateCredentialPermission("cred1 pc1 \"pn1\" pc2 \"pn2\"", "read");
            oos.writeObject(pcp);
            oos.flush();
            fos.close();
            FileInputStream fis = new FileInputStream("serial.tmp");
            ObjectInputStream ois = new ObjectInputStream(fis);
            PrivateCredentialPermission pcp2 = (PrivateCredentialPermission) ois.readObject();
            fis.close();
            System.out.println("pcp2 = " + pcp2.toString());
            System.out.println("pcp2.getPrincipals().length = " + pcp2.getPrincipals().length);
            if (!pcp.equals(pcp2) || !pcp2.equals(pcp)) {
                throw new SecurityException("Serial test failed: " + "EQUALS TEST FAILED");
            }
            System.out.println("Serial test succeeded");
        } catch (Exception e) {
            e.printStackTrace();
            throw new SecurityException("Serial test failed");
        }
    }
}
