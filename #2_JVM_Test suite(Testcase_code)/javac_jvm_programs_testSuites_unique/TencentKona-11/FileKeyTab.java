

import java.io.File;
import sun.security.krb5.PrincipalName;
import sun.security.krb5.internal.ktab.KeyTab;

public class FileKeyTab {
    public static void main(String[] args) throws Exception {
        String name = "ktab";
        KeyTab kt = KeyTab.create(name);
        kt.addEntry(new PrincipalName("a@A"), "x".toCharArray(), 1, true);
        kt.save();
        check(name);
        check("FILE:" + name);

        name = new File(name).getAbsolutePath().toString();

        check(name);
        check("FILE:" + name);

        
        
        check("FILE:/" + name);
    }

    static void check(String file) throws Exception {
        System.out.println("Checking for " + file + "...");
        KeyTab kt2 = KeyTab.getInstance(file);
        if (kt2.isMissing()) {
            throw new Exception("FILE:ktab cannot be loaded");
        }
    }
}
