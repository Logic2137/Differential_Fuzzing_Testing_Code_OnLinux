import java.security.*;
import java.security.cert.*;
import java.util.*;
import java.io.*;

public class EntryMethods extends Provider implements KeyStore.Entry {

    private static FileInputStream pre15fis;

    private static char[] password = { 'f', 'o', 'o', 'b', 'a', 'r' };

    private static char[] badPwd = { 'b', 'a', 'd', 'p', 'w', 'd' };

    public static class FooProtect implements KeyStore.ProtectionParameter {
    }

    public static class FooParameter implements KeyStore.LoadStoreParameter {

        public KeyStore.ProtectionParameter getProtectionParameter() {
            return null;
        }
    }

    public static class MyLoadStoreParameter implements KeyStore.LoadStoreParameter {

        private KeyStore.ProtectionParameter protection;

        MyLoadStoreParameter(KeyStore.ProtectionParameter protection) {
            this.protection = protection;
        }

        public KeyStore.ProtectionParameter getProtectionParameter() {
            return protection;
        }
    }

    public static class FooEntry implements KeyStore.Entry {
    }

    public EntryMethods() throws Exception {
        super("EntryMethods", "0.0", "EntryMethods");
        pre15fis = new FileInputStream(System.getProperty("test.src") + "/EntryMethods.pre15.keystore");
        AccessController.doPrivileged(new PrivilegedAction() {

            public Object run() {
                put("KeyStore.Pre15KeyStore", "EntryMethods$Pre15");
                put("KeyStore.Post15KeyStore", "EntryMethods$Post15");
                put("KeyStore.UnrecoverableKeyStore", "EntryMethods$UnrecoverableKS");
                return null;
            }
        });
    }

    public static void main(String[] args) throws Exception {
        EntryMethods entry = new EntryMethods();
        KeyStore pre15KS = KeyStore.getInstance("Pre15KeyStore", entry);
        testPre15(pre15KS);
        KeyStore post15KS = KeyStore.getInstance("Post15KeyStore", entry);
        testPost15(post15KS);
        KeyStore uKS = KeyStore.getInstance("UnrecoverableKeyStore", entry);
        testUnrecoverable(uKS);
    }

    private static void testPre15(KeyStore ks) throws Exception {
        int tNum = 1;
        KeyStore.Entry e = null;
        ks.load((KeyStore.LoadStoreParameter) null);
        System.out.println("[Pre1.5] test " + tNum++ + " passed");
        try {
            ks.load(new FooParameter());
            throw new SecurityException("[Pre1.5] test " + tNum + " failed");
        } catch (UnsupportedOperationException uoe) {
            System.out.println("[Pre1.5] test " + tNum++ + " passed");
        } catch (NoSuchAlgorithmException nsae) {
            System.out.println("[Pre1.5] test " + tNum++ + " passed");
        }
        ks.load(new MyLoadStoreParameter(new KeyStore.PasswordProtection(password)));
        System.out.println("[Pre1.5] test " + tNum++ + " passed");
        ks.load(pre15fis, password);
        KeyStore.Entry pkeNew = ks.getEntry("privkey", new KeyStore.PasswordProtection(password));
        KeyStore.Entry tceNew = ks.getEntry("trustedcert", null);
        try {
            ks.store(new FooParameter());
            throw new SecurityException("[Pre1.5] test " + tNum + " failed");
        } catch (UnsupportedOperationException uoe) {
            System.out.println("[Pre1.5] test " + tNum++ + " passed");
        }
        try {
            ks.store(null);
            throw new SecurityException("[Pre1.5] test " + tNum + " failed");
        } catch (UnsupportedOperationException uoe) {
            System.out.println("[Pre1.5] test " + tNum++ + " passed");
        }
        e = ks.getEntry("notPresent", new KeyStore.PasswordProtection(password));
        if (e == null) {
            System.out.println("[Pre1.5] test " + tNum++ + " passed");
        } else {
            throw new SecurityException("[Pre1.5] test " + tNum + " failed - " + "expected null entry returned");
        }
        try {
            e = ks.getEntry("privkey", null);
            throw new SecurityException("[Pre1.5] test " + tNum + " failed - " + "expected UnrecoverableEntryException");
        } catch (UnrecoverableEntryException uee) {
            System.out.println("[Pre1.5] test " + tNum++ + " passed");
        }
        try {
            e = ks.getEntry("privkey", new KeyStore.PasswordProtection(badPwd));
            throw new SecurityException("[Pre1.5] test " + tNum + " failed - " + "expected UnrecoverableEntryException");
        } catch (UnrecoverableEntryException uee) {
            System.out.println("[Pre1.5] test " + tNum++ + " passed");
        }
        try {
            e = ks.getEntry("privkey", new FooProtect());
            throw new SecurityException("[Pre1.5] test " + tNum + " failed - " + "expected UnsupportedOperationException");
        } catch (UnsupportedOperationException uoe) {
            System.out.println("[Pre1.5] test " + tNum++ + " passed");
        }
        e = ks.getEntry("privkey", new KeyStore.PasswordProtection(password));
        if (e instanceof KeyStore.PrivateKeyEntry) {
            System.out.println("[Pre1.5] test " + tNum++ + " passed");
        } else {
            throw new SecurityException("[Pre1.5] test " + tNum + " failed - " + "expected PrivateKeyEntry");
        }
        e = ks.getEntry("trustedcert", null);
        if (e instanceof KeyStore.TrustedCertificateEntry) {
            System.out.println("[Pre1.5] test " + tNum++ + " passed");
        } else {
            throw new SecurityException("[Pre1.5] test " + tNum + " failed");
        }
        try {
            e = ks.getEntry("trustedcert", new KeyStore.PasswordProtection(password));
            throw new SecurityException("[Pre1.5] test " + tNum + " failed");
        } catch (UnsupportedOperationException uoe) {
            System.out.println("[Pre1.5] test " + tNum++ + " passed");
        }
        try {
            ks.setEntry("foo", new FooEntry(), new KeyStore.PasswordProtection(password));
            throw new SecurityException("[Pre1.5] test " + tNum + " failed - " + "expected KeyStoreException");
        } catch (KeyStoreException kse) {
            System.out.println("[Pre1.5] test " + tNum++ + " passed");
        }
        try {
            ks.setEntry("newPrivKey", pkeNew, null);
            throw new SecurityException("[Pre1.5] test " + tNum + " failed - " + "expected KeyStoreException");
        } catch (KeyStoreException kse) {
            System.out.println("[Pre1.5] test " + tNum++ + " passed");
        }
        try {
            ks.setEntry("newPrivKey", pkeNew, new FooProtect());
            throw new SecurityException("[Pre1.5] test " + tNum + " failed - " + "expected KeyStoreException");
        } catch (KeyStoreException kse) {
            System.out.println("[Pre1.5] test " + tNum++ + " passed");
        }
        ks.setEntry("newPrivKey", pkeNew, new KeyStore.PasswordProtection(password));
        e = ks.getEntry("newPrivKey", new KeyStore.PasswordProtection(password));
        if (e instanceof KeyStore.PrivateKeyEntry) {
            System.out.println("[Pre1.5] test " + tNum++ + " passed");
        } else {
            throw new SecurityException("[Pre1.5] test " + tNum + " failed - " + "expected PrivateKeyEntry");
        }
        try {
            ks.setEntry("newTrustedcert", tceNew, new KeyStore.PasswordProtection(password));
            throw new SecurityException("[Pre1.5] test " + tNum + " failed - " + "expected KeyStoreException");
        } catch (KeyStoreException kse) {
            System.out.println("[Pre1.5] test " + tNum++ + " passed");
        }
        ks.setEntry("newTrustedcert", tceNew, null);
        e = ks.getEntry("newTrustedcert", null);
        if (e instanceof KeyStore.TrustedCertificateEntry) {
            System.out.println("[Pre1.5] test " + tNum++ + " passed");
        } else {
            throw new SecurityException("[Pre1.5] test " + tNum + " failed - " + "expected TrustedCertificateEntry");
        }
        if (ks.entryInstanceOf("foo", EntryMethods.class) == false) {
            System.out.println("[Pre1.5] test " + tNum++ + " passed");
        } else {
            throw new SecurityException("[Pre1.5] test " + tNum + " failed");
        }
        if (ks.entryInstanceOf("privkey", EntryMethods.class) == false) {
            System.out.println("[Pre1.5] test " + tNum++ + " passed");
        } else {
            throw new SecurityException("[Pre1.5] test " + tNum + " failed");
        }
        if (ks.entryInstanceOf("trustedcert", KeyStore.TrustedCertificateEntry.class)) {
            System.out.println("[Pre1.5] test " + tNum++ + " passed");
        } else {
            throw new SecurityException("[Pre1.5] test " + tNum + " failed");
        }
        if (ks.entryInstanceOf("privkey", KeyStore.PrivateKeyEntry.class)) {
            System.out.println("[Pre1.5] test " + tNum++ + " passed");
        } else {
            throw new SecurityException("[Pre1.5] test " + tNum + " failed");
        }
    }

    private static void testPost15(KeyStore ks) throws Exception {
        KeyStore.Entry e = null;
        ks.load(new EntryMethods.FooParameter());
        ks.store(new EntryMethods.FooParameter());
        e = ks.getEntry("foo", new KeyStore.PasswordProtection(password));
        if (!(e instanceof EntryMethods.FooEntry)) {
            throw new SecurityException("testPost15 getEntry(String, ProtParm) " + "expected EntryMethods.FooEntry returned");
        }
        ks.setEntry("foo", new EntryMethods.FooEntry(), new KeyStore.PasswordProtection(password));
        if (!ks.entryInstanceOf("foo", KeyStore.PrivateKeyEntry.class)) {
            throw new SecurityException("testPost15 entryInstanceOf(String, Class) " + "expected true returned");
        }
        System.out.println("[Post1.5] tests all passed");
    }

    private static void testUnrecoverable(KeyStore ks) throws Exception {
        ks.load(new EntryMethods.FooParameter());
        try {
            ks.getEntry("foo", new KeyStore.PasswordProtection(password));
            throw new SecurityException("UnrecoverableEntryException not thrown for " + "getEntry(String, ProtectionParam)");
        } catch (UnrecoverableEntryException uee) {
            System.out.println("[UnrecoverableEntry] test passed");
        }
    }

    public static class Pre15 extends KeyStoreSpi {

        private static KeyStore jks = getJKS();

        private static KeyStore getJKS() {
            try {
                return (KeyStore) KeyStore.getInstance("JKS");
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        public Pre15() {
        }

        public Key engineGetKey(String alias, char[] password) throws NoSuchAlgorithmException, UnrecoverableKeyException {
            try {
                return jks.getKey(alias, password);
            } catch (KeyStoreException ke) {
                throw new RuntimeException("Unexpected exception", ke);
            }
        }

        public java.security.cert.Certificate[] engineGetCertificateChain(String alias) {
            try {
                return jks.getCertificateChain(alias);
            } catch (KeyStoreException ke) {
                throw new RuntimeException("Unexpected exception", ke);
            }
        }

        public java.security.cert.Certificate engineGetCertificate(String alias) {
            try {
                return jks.getCertificate(alias);
            } catch (KeyStoreException ke) {
                throw new RuntimeException("Unexpected exception", ke);
            }
        }

        public Date engineGetCreationDate(String alias) {
            try {
                return jks.getCreationDate(alias);
            } catch (KeyStoreException ke) {
                throw new RuntimeException("Unexpected exception", ke);
            }
        }

        public void engineSetKeyEntry(String alias, Key key, char[] password, java.security.cert.Certificate[] chain) throws KeyStoreException {
            jks.setKeyEntry(alias, key, password, chain);
        }

        public void engineSetKeyEntry(String alias, byte[] key, java.security.cert.Certificate[] chain) throws KeyStoreException {
            jks.setKeyEntry(alias, key, chain);
        }

        public void engineSetCertificateEntry(String alias, java.security.cert.Certificate cert) throws KeyStoreException {
            jks.setCertificateEntry(alias, cert);
        }

        public void engineDeleteEntry(String alias) throws KeyStoreException {
            jks.deleteEntry(alias);
        }

        public Enumeration engineAliases() {
            try {
                return jks.aliases();
            } catch (KeyStoreException ke) {
                throw new RuntimeException("Unexpected exception", ke);
            }
        }

        public boolean engineContainsAlias(String alias) {
            try {
                return jks.containsAlias(alias);
            } catch (KeyStoreException ke) {
                throw new RuntimeException("Unexpected exception", ke);
            }
        }

        public int engineSize() {
            try {
                return jks.size();
            } catch (KeyStoreException ke) {
                throw new RuntimeException("Unexpected exception", ke);
            }
        }

        public boolean engineIsKeyEntry(String alias) {
            try {
                return jks.isKeyEntry(alias);
            } catch (KeyStoreException ke) {
                throw new RuntimeException("Unexpected exception", ke);
            }
        }

        public boolean engineIsCertificateEntry(String alias) {
            try {
                return jks.isCertificateEntry(alias);
            } catch (KeyStoreException ke) {
                throw new RuntimeException("Unexpected exception", ke);
            }
        }

        public String engineGetCertificateAlias(java.security.cert.Certificate cert) {
            try {
                return jks.getCertificateAlias(cert);
            } catch (KeyStoreException ke) {
                throw new RuntimeException("Unexpected exception", ke);
            }
        }

        public void engineStore(OutputStream stream, char[] password) throws IOException, NoSuchAlgorithmException, CertificateException {
            try {
                jks.store(stream, password);
            } catch (KeyStoreException ke) {
                throw new RuntimeException("Unexpected exception", ke);
            }
        }

        public void engineLoad(InputStream stream, char[] password) throws IOException, NoSuchAlgorithmException, CertificateException {
            jks.load(stream, password);
        }
    }

    public static class Post15 extends Pre15 {

        public void engineStore(KeyStore.LoadStoreParameter parameter) throws IOException, NoSuchAlgorithmException, CertificateException {
            if (!(parameter instanceof EntryMethods.FooParameter)) {
                throw new IOException("Post15 engineStore method expected " + "FooParameter");
            }
        }

        public void engineLoad(KeyStore.LoadStoreParameter parameter) throws IOException, NoSuchAlgorithmException, CertificateException {
            if (!(parameter instanceof EntryMethods.FooParameter)) {
                throw new IOException("Post15 engineLoadFrom method expected " + "FooParameter");
            }
        }

        public KeyStore.Entry engineGetEntry(String alias, KeyStore.ProtectionParameter protectionParam) throws UnrecoverableEntryException {
            if (!alias.equals("foo")) {
                throw new SecurityException("Post15 engineGetEntry(String, ProtectionParam) " + "expected [foo] alias");
            }
            KeyStore.PasswordProtection pwdParam = (KeyStore.PasswordProtection) protectionParam;
            if (pwdParam.getPassword().length != 6) {
                throw new SecurityException("Post15 engineGetEntry(String, ProtectionParam) " + "expected [foobar] password");
            }
            return new EntryMethods.FooEntry();
        }

        public void engineSetEntry(String alias, KeyStore.Entry entry, KeyStore.ProtectionParameter protectionParam) {
            if (!alias.equals("foo") || !(entry instanceof EntryMethods.FooEntry)) {
                throw new SecurityException("Post15 engineSetEntry(String, entry, ProtParm) " + "expected [foo] alias and EntryMethods.FooEntry");
            }
            KeyStore.PasswordProtection pwdParam = (KeyStore.PasswordProtection) protectionParam;
            if (pwdParam.getPassword().length != 6) {
                throw new SecurityException("Post15 engineSetEntry(String, entry, ProtParm) " + "expected [foobar] password");
            }
        }

        public boolean engineEntryInstanceOf(String alias, Class<? extends KeyStore.Entry> entryClass) {
            if (!alias.equals("foo") || entryClass != KeyStore.PrivateKeyEntry.class) {
                throw new SecurityException("Post15 engineEntryInstanceOf(String, Class) " + "expected [foo] alias " + "and [KeyStore.PrivateKeyEntry] class");
            }
            return true;
        }
    }

    public static class UnrecoverableKS extends Post15 {

        public KeyStore.Entry engineGetEntry(String alias, KeyStore.ProtectionParameter protectionParam) throws UnrecoverableEntryException {
            throw new UnrecoverableEntryException();
        }
    }
}
