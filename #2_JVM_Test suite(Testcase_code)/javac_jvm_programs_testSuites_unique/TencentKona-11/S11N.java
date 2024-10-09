



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import sun.security.util.ObjectIdentifier;

public class S11N {
    static String[] SMALL= {
        "0.0",
        "1.1",
        "2.2",
        "1.2.3456",
        "1.2.2147483647.4",
        "1.2.268435456.4",
    };

    static String[] HUGE = {
        "2.16.764.1.3101555394.1.0.100.2.1",
        "1.2.2147483648.4",
        "2.3.4444444444444444444444",
        "1.2.8888888888888888.33333333333333333.44444444444444",
    };

    public static void main(String[] args) throws Exception {
        if (args[0].equals("check")) {
            String jv = System.getProperty("java.version");
            
            String [] va = (jv.split("-")[0]).split("\\.");
            String v = (va.length == 1 || !va[0].equals("1")) ? va[0] : va[1];
            int version = Integer.valueOf(v);
            System.out.println("version is " + version);
            if (version >= 7) {
                for (String oid: SMALL) {
                    
                    check(out(oid), oid);
                    
                    check(out6(oid), oid);
                }
                for (String oid: HUGE) {
                    
                    check(out(oid), oid);
                }
            } else {
                for (String oid: SMALL) {
                    
                    check(out(oid), oid);
                    
                    check(out7(oid), oid);
                }
                for (String oid: HUGE) {
                    
                    boolean ok = false;
                    try {
                        check(out7(oid), oid);
                        ok = true;
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    if (ok) {
                        throw new Exception();
                    }
                }
            }
        } else {
            
            
            
            
            dump(args[0], SMALL);
            
            dump(args[0], HUGE);
        }
    }

    
    private static byte[] out6(String oid) throws Exception {
        return decode(dump6.get(oid));
    }

    
    private static byte[] out7(String oid) throws Exception {
        return decode(dump7.get(oid));
    }

    
    private static byte[] out(String oid) throws Exception {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        new ObjectOutputStream(bout).writeObject(new ObjectIdentifier(oid));
        return bout.toByteArray();
    }

    
    private static void check(byte[] in, String oid) throws Exception {
        ObjectIdentifier o = (ObjectIdentifier) (
                new ObjectInputStream(new ByteArrayInputStream(in)).readObject());
        if (!o.toString().equals(oid)) {
            throw new Exception("Read Fail " + o + ", not " + oid);
        }
    }

    
    private static void dump(String title, String[] oids) throws Exception {
        for (String oid: oids) {
            String hex = encode(out(oid));
            System.out.println("        " + title + ".put(\"" + oid + "\",");
            for (int i = 0; i<hex.length(); i+= 64) {
                int end = i + 64;
                if (end > hex.length()) {
                    end = hex.length();
                }
                System.out.print("            \"" + hex.substring(i, end) + "\"");
                if (end == hex.length()) {
                    System.out.println(");");
                } else {
                    System.out.println(" +");
                }
            }
        }
    }

    private static String encode(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b: bytes) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }

    private static byte[] decode(String var) {
        byte[] data = new byte[var.length()/2];
        for (int i=0; i<data.length; i++) {
            data[i] = Integer.valueOf(var.substring(2 * i, 2 * i + 2), 16).byteValue();
        }
        return data;
    }

    
    private static Map<String,String> dump7 = new HashMap<String,String>();
    private static Map<String,String> dump6 = new HashMap<String,String>();

    static {
        
        dump7.put("0.0",
            "aced00057372002273756e2e73656375726974792e7574696c2e4f626a656374" +
            "4964656e74696669657278b20eec64177f2e03000349000c636f6d706f6e656e" +
            "744c656e4c000a636f6d706f6e656e74737400124c6a6176612f6c616e672f4f" +
            "626a6563743b5b0008656e636f64696e677400025b4278700000000275720002" +
            "5b494dba602676eab2a50200007870000000020000000000000000757200025b" +
            "42acf317f8060854e00200007870000000010078");
        dump7.put("1.1",
            "aced00057372002273756e2e73656375726974792e7574696c2e4f626a656374" +
            "4964656e74696669657278b20eec64177f2e03000349000c636f6d706f6e656e" +
            "744c656e4c000a636f6d706f6e656e74737400124c6a6176612f6c616e672f4f" +
            "626a6563743b5b0008656e636f64696e677400025b4278700000000275720002" +
            "5b494dba602676eab2a50200007870000000020000000100000001757200025b" +
            "42acf317f8060854e00200007870000000012978");
        dump7.put("2.2",
            "aced00057372002273756e2e73656375726974792e7574696c2e4f626a656374" +
            "4964656e74696669657278b20eec64177f2e03000349000c636f6d706f6e656e" +
            "744c656e4c000a636f6d706f6e656e74737400124c6a6176612f6c616e672f4f" +
            "626a6563743b5b0008656e636f64696e677400025b4278700000000275720002" +
            "5b494dba602676eab2a50200007870000000020000000200000002757200025b" +
            "42acf317f8060854e00200007870000000015278");
        dump7.put("1.2.3456",
            "aced00057372002273756e2e73656375726974792e7574696c2e4f626a656374" +
            "4964656e74696669657278b20eec64177f2e03000349000c636f6d706f6e656e" +
            "744c656e4c000a636f6d706f6e656e74737400124c6a6176612f6c616e672f4f" +
            "626a6563743b5b0008656e636f64696e677400025b4278700000000375720002" +
            "5b494dba602676eab2a5020000787000000003000000010000000200000d8075" +
            "7200025b42acf317f8060854e00200007870000000032a9b0078");
        dump7.put("1.2.2147483647.4",
            "aced00057372002273756e2e73656375726974792e7574696c2e4f626a656374" +
            "4964656e74696669657278b20eec64177f2e03000349000c636f6d706f6e656e" +
            "744c656e4c000a636f6d706f6e656e74737400124c6a6176612f6c616e672f4f" +
            "626a6563743b5b0008656e636f64696e677400025b4278700000000475720002" +
            "5b494dba602676eab2a502000078700000000400000001000000027fffffff00" +
            "000004757200025b42acf317f8060854e00200007870000000072a87ffffff7f" +
            "0478");
        dump7.put("1.2.268435456.4",
            "aced00057372002273756e2e73656375726974792e7574696c2e4f626a656374" +
            "4964656e74696669657278b20eec64177f2e03000349000c636f6d706f6e656e" +
            "744c656e4c000a636f6d706f6e656e74737400124c6a6176612f6c616e672f4f" +
            "626a6563743b5b0008656e636f64696e677400025b4278700000000475720002" +
            "5b494dba602676eab2a502000078700000000400000001000000021000000000" +
            "000004757200025b42acf317f8060854e00200007870000000072a8180808000" +
            "0478");
        dump7.put("2.16.764.1.3101555394.1.0.100.2.1",
            "aced00057372002273756e2e73656375726974792e7574696c2e4f626a656374" +
            "4964656e74696669657278b20eec64177f2e03000349000c636f6d706f6e656e" +
            "744c656e4c000a636f6d706f6e656e74737400124c6a6176612f6c616e672f4f" +
            "626a6563743b5b0008656e636f64696e677400025b427870ffffffff7372003e" +
            "73756e2e73656375726974792e7574696c2e4f626a6563744964656e74696669" +
            "657224487567654f69644e6f74537570706f7274656442794f6c644a444b0000" +
            "0000000000010200007870757200025b42acf317f8060854e002000078700000" +
            "000e60857c018bc6f7f542010064020178");
        dump7.put("1.2.2147483648.4",
            "aced00057372002273756e2e73656375726974792e7574696c2e4f626a656374" +
            "4964656e74696669657278b20eec64177f2e03000349000c636f6d706f6e656e" +
            "744c656e4c000a636f6d706f6e656e74737400124c6a6176612f6c616e672f4f" +
            "626a6563743b5b0008656e636f64696e677400025b427870ffffffff7372003e" +
            "73756e2e73656375726974792e7574696c2e4f626a6563744964656e74696669" +
            "657224487567654f69644e6f74537570706f7274656442794f6c644a444b0000" +
            "0000000000010200007870757200025b42acf317f8060854e002000078700000" +
            "00072a88808080000478");
        dump7.put("2.3.4444444444444444444444",
            "aced00057372002273756e2e73656375726974792e7574696c2e4f626a656374" +
            "4964656e74696669657278b20eec64177f2e03000349000c636f6d706f6e656e" +
            "744c656e4c000a636f6d706f6e656e74737400124c6a6176612f6c616e672f4f" +
            "626a6563743b5b0008656e636f64696e677400025b427870ffffffff7372003e" +
            "73756e2e73656375726974792e7574696c2e4f626a6563744964656e74696669" +
            "657224487567654f69644e6f74537570706f7274656442794f6c644a444b0000" +
            "0000000000010200007870757200025b42acf317f8060854e002000078700000" +
            "000c5383e1ef87a4d1bdebc78e1c78");
        dump7.put("1.2.8888888888888888.33333333333333333.44444444444444",
            "aced00057372002273756e2e73656375726974792e7574696c2e4f626a656374" +
            "4964656e74696669657278b20eec64177f2e03000349000c636f6d706f6e656e" +
            "744c656e4c000a636f6d706f6e656e74737400124c6a6176612f6c616e672f4f" +
            "626a6563743b5b0008656e636f64696e677400025b427870ffffffff7372003e" +
            "73756e2e73656375726974792e7574696c2e4f626a6563744964656e74696669" +
            "657224487567654f69644e6f74537570706f7274656442794f6c644a444b0000" +
            "0000000000010200007870757200025b42acf317f8060854e002000078700000" +
            "00182a8fe58cdbc5ae9c38bb9b8fd7a48daa558a8dc0bacb8e1c78");

        dump6.put("0.0",
            "aced00057372002273756e2e73656375726974792e7574696c2e4f626a656374" +
            "4964656e74696669657278b20eec64177f2e02000249000c636f6d706f6e656e" +
            "744c656e5b000a636f6d706f6e656e74737400025b4978700000000275720002" +
            "5b494dba602676eab2a50200007870000000020000000000000000");
        dump6.put("1.1",
            "aced00057372002273756e2e73656375726974792e7574696c2e4f626a656374" +
            "4964656e74696669657278b20eec64177f2e02000249000c636f6d706f6e656e" +
            "744c656e5b000a636f6d706f6e656e74737400025b4978700000000275720002" +
            "5b494dba602676eab2a50200007870000000020000000100000001");
        dump6.put("2.2",
            "aced00057372002273756e2e73656375726974792e7574696c2e4f626a656374" +
            "4964656e74696669657278b20eec64177f2e02000249000c636f6d706f6e656e" +
            "744c656e5b000a636f6d706f6e656e74737400025b4978700000000275720002" +
            "5b494dba602676eab2a50200007870000000020000000200000002");
        dump6.put("1.2.3456",
            "aced00057372002273756e2e73656375726974792e7574696c2e4f626a656374" +
            "4964656e74696669657278b20eec64177f2e02000249000c636f6d706f6e656e" +
            "744c656e5b000a636f6d706f6e656e74737400025b4978700000000375720002" +
            "5b494dba602676eab2a5020000787000000003000000010000000200000d80");
        dump6.put("1.2.2147483647.4",
            "aced00057372002273756e2e73656375726974792e7574696c2e4f626a656374" +
            "4964656e74696669657278b20eec64177f2e02000249000c636f6d706f6e656e" +
            "744c656e5b000a636f6d706f6e656e74737400025b4978700000000475720002" +
            "5b494dba602676eab2a502000078700000000400000001000000027fffffff00" +
            "000004");
        dump6.put("1.2.268435456.4",
            "aced00057372002273756e2e73656375726974792e7574696c2e4f626a656374" +
            "4964656e74696669657278b20eec64177f2e02000249000c636f6d706f6e656e" +
            "744c656e5b000a636f6d706f6e656e74737400025b4978700000000475720002" +
            "5b494dba602676eab2a502000078700000000400000001000000021000000000" +
            "000004");

        
    }
}
