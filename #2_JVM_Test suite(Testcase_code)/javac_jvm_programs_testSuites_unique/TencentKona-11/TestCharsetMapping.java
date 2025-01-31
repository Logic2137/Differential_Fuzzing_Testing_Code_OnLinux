



import java.io.*;
import java.nio.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.util.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;

public class TestCharsetMapping {

    private static final int BUFSIZ = 8192;     
    private static final int MAXERRS = 10;      

    private static final PrintStream log = System.out;

    
    private static boolean verbose = false;

    
    private static final int ENCODE = 1;
    private static final int DECODE = 2;

    
    private static ByteBuffer expand(ByteBuffer bb) {
        ByteBuffer nbb = ByteBuffer.allocate(bb.capacity() * 2);
        bb.flip();
        nbb.put(bb);
        return nbb;
    }

    private static CharBuffer expand(CharBuffer cb) {
        CharBuffer ncb = CharBuffer.allocate(cb.capacity() * 2);
        cb.flip();
        ncb.put(cb);
        return ncb;
    }

    private static byte[] parseBytes(String s) {
        int nb = s.length() / 2;
        byte[] bs = new byte[nb];
        for (int i = 0; i < nb; i++) {
            int j = i * 2;
            if (j + 2 > s.length())
                throw new RuntimeException("Malformed byte string: " + s);
            bs[i] = (byte)Integer.parseInt(s.substring(j, j + 2), 16);
        }
        return bs;
    }

    private static String printBytes(byte[] bs) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bs.length; i++) {
            sb.append(Integer.toHexString((bs[i] >> 4) & 0xf));
            sb.append(Integer.toHexString((bs[i] >> 0) & 0xf));
        }
        return sb.toString();
    }

    private static String printCodePoint(int cp) {
        StringBuffer sb = new StringBuffer();
        sb.append("U+");
        if (cp > 0xffff)
            sb.append(Integer.toHexString((cp >> 16) & 0xf));
        sb.append(Integer.toHexString((cp >> 12) & 0xf));
        sb.append(Integer.toHexString((cp >> 8) & 0xf));
        sb.append(Integer.toHexString((cp >> 4) & 0xf));
        sb.append(Integer.toHexString((cp >> 0) & 0xf));
        return sb.toString();
    }

    private static int getCodePoint(CharBuffer cb) {
        char c = cb.get();
        if (Character.isHighSurrogate(c))
            return Character.toCodePoint(c, cb.get());
        else
            return c;
    }

    private static String plural(int n) {
        return (n == 1 ? "" : "s");
    }

    
    private CharsetInfo csinfo;
    private CharsetDecoder decoder = null;
    private CharsetEncoder encoder = null;

    
    
    
    
    
    private boolean shiftHackDBCS = false;

    private TestCharsetMapping(CharsetInfo csinfo) throws Exception {
        this.csinfo = csinfo;
        this.encoder = csinfo.cs.newEncoder()
            .onUnmappableCharacter(CodingErrorAction.REPLACE)
            .onMalformedInput(CodingErrorAction.REPLACE);
        this.decoder = csinfo.cs.newDecoder()
            .onUnmappableCharacter(CodingErrorAction.REPLACE)
            .onMalformedInput(CodingErrorAction.REPLACE);
    }

    private class Test {
        
        
        private int bytesPerChar;

        
        private ByteBuffer refBytes = ByteBuffer.allocate(BUFSIZ);
        private CharBuffer refChars = CharBuffer.allocate(BUFSIZ);

        private ByteBuffer dRefBytes = ByteBuffer.allocateDirect(BUFSIZ);
        private CharBuffer dRefChars = ByteBuffer.allocateDirect(BUFSIZ*2).asCharBuffer();

        private Test(int bpc) {
            bytesPerChar = bpc;
        }

        
        private void put(byte[] bs) {
            if (refBytes.remaining() < bytesPerChar)
                refBytes = expand(refBytes);
            refBytes.put(bs);
        }

        private void put(byte[] bs, char[] cc) {
            if (bs.length != bytesPerChar)
                throw new IllegalArgumentException(bs.length
                                                   + " != "
                                                   + bytesPerChar);
            if (refBytes.remaining() < bytesPerChar)
                refBytes = expand(refBytes);
            refBytes.put(bs);
            if (refChars.remaining() < cc.length)
                refChars = expand(refChars);
            refChars.put(cc);
        }

        private boolean decode(ByteBuffer refBytes, CharBuffer refChars)
            throws Exception {
            log.println("    decode" + (refBytes.isDirect()?" (direct)":""));
            CharBuffer out = decoder.decode(refBytes);

            refBytes.rewind();
            byte[] bs = new byte[bytesPerChar];
            int e = 0;

            if (shiftHackDBCS && bytesPerChar == 2 && refBytes.get() != (byte)0x0e) {
                log.println("Missing leading byte");
            }

            while (refChars.hasRemaining()) {
                refBytes.get(bs);
                int rcp = getCodePoint(refChars);
                int ocp = getCodePoint(out);
                if (rcp != ocp) {
                    log.println("      Error: "
                                + printBytes(bs)
                                + " --> "
                                + printCodePoint(ocp)
                                + ", expected "
                                + printCodePoint(rcp));
                    if (++e >= MAXERRS) {
                        log.println("      Too many errors, giving up");
                        break;
                    }
                }
                if (verbose) {
                    log.println("      "
                                + printBytes(bs)
                                + " --> "
                                + printCodePoint(rcp));
                }
            }

            if (shiftHackDBCS && bytesPerChar == 2 && refBytes.get() != (byte)0x0f) {
                log.println("Missing trailing byte");
            }

            if (e == 0 && (refChars.hasRemaining() || out.hasRemaining())) {
                
                throw new IllegalStateException();
            }
            refBytes.rewind();
            refChars.rewind();
            return (e == 0);
        }

        private boolean encode(ByteBuffer refBytes, CharBuffer refChars)
            throws Exception {
            log.println("    encode" + (refBytes.isDirect()?" (direct)":""));
            ByteBuffer out = encoder.encode(refChars);
            refChars.rewind();

            if (shiftHackDBCS && bytesPerChar == 2 && out.get() != refBytes.get()) {
                log.println("Missing leading byte");
                return false;
            }

            byte[] rbs = new byte[bytesPerChar];
            byte[] obs = new byte[bytesPerChar];
            int e = 0;
            while (refChars.hasRemaining()) {
                int cp = getCodePoint(refChars);
                refBytes.get(rbs);
                out.get(obs);
                boolean eq = true;
                for (int i = 0; i < bytesPerChar; i++)
                    eq &= rbs[i] == obs[i];
                if (!eq) {
                    log.println("      Error: "
                                + printCodePoint(cp)
                                + " --> "
                                + printBytes(obs)
                                + ", expected "
                                + printBytes(rbs));
                    if (++e >= MAXERRS) {
                        log.println("      Too many errors, giving up");
                        break;
                    }
                }
                if (verbose) {
                    log.println("      "
                                + printCodePoint(cp)
                                + " --> "
                                + printBytes(rbs));
                }
            }

            if (shiftHackDBCS && bytesPerChar == 2 && out.get() != refBytes.get()) {
                log.println("Missing trailing byte");
                return false;
            }

            if (e == 0 && (refBytes.hasRemaining() || out.hasRemaining())) {
                
                throw new IllegalStateException();
            }

            refBytes.rewind();
            refChars.rewind();
            return (e == 0);
        }

        private boolean run(int mode) throws Exception {
            log.println("  " + bytesPerChar
                        + " byte" + plural(bytesPerChar) + "/char");

            if (dRefBytes.capacity() < refBytes.capacity()) {
                dRefBytes = ByteBuffer.allocateDirect(refBytes.capacity());
            }
            if (dRefChars.capacity() < refChars.capacity()) {
                dRefChars = ByteBuffer.allocateDirect(refChars.capacity()*2)
                                      .asCharBuffer();
            }
            refBytes.flip();
            refChars.flip();
            dRefBytes.clear();
            dRefChars.clear();

            dRefBytes.put(refBytes).flip();
            dRefChars.put(refChars).flip();
            refBytes.flip();
            refChars.flip();

            boolean rv = true;
            if (mode != ENCODE) {
                rv &= decode(refBytes, refChars);
                rv &= decode(dRefBytes, dRefChars);
            }
            if (mode != DECODE) {
                rv &= encode(refBytes, refChars);
                rv &= encode(dRefBytes, dRefChars);
            }
            return rv;
        }
    }

    
    private int maxBytesPerChar = 0;

    
    private Test[] tests;

    private void clearTests() {
        maxBytesPerChar = 0;
        tests = new Test[0];
    }

    
    
    
    private Test testFor(int bpc) {
        if (bpc > maxBytesPerChar) {
            Test[] ts = new Test[bpc];
            System.arraycopy(tests, 0, ts, 0, maxBytesPerChar);
            for (int i = maxBytesPerChar; i < bpc; i++)
                ts[i] = new Test(i + 1);
            tests = ts;
            maxBytesPerChar = bpc;
        }
        return tests[bpc - 1];
    }

    private boolean testStringConv() throws Exception {
        if (shiftHackDBCS) {
            log.println("  string de/encoding   skipped for ebcdic");
            return true;
        }
        boolean rv = true;
        log.println("  string de/encoding");
        
        ByteArrayOutputStream baosDec = new ByteArrayOutputStream();
        StringBuilder sbDec = new StringBuilder();
        
        ByteArrayOutputStream baosEnc = new ByteArrayOutputStream();
        StringBuilder sbEnc = new StringBuilder();

        for (Entry e : csinfo.mappings) {
            baosDec.write(e.bs);
            sbDec.append(Character.toChars(e.cp));
            if (e.cp2 != 0)
                sbDec.append(e.cp2);

            
            if (csinfo.nr != null && csinfo.nr.containsKey(e.bb) ||
                csinfo.c2b != null && !csinfo.c2b.containsKey(e.cp))
                continue;
            baosEnc.write(e.bs);
            sbEnc.append(Character.toChars(e.cp));
            if (e.cp2 != 0)
                sbEnc.append(e.cp2);
        }
        log.println("    new String()");
        if (!new String(baosDec.toByteArray(), csinfo.csName).equals(sbDec.toString())) {
            log.println("      Error: new String() failed");
            rv = false;
        }
        log.println("    String.getBytes()");
        if (!Arrays.equals(baosEnc.toByteArray(), sbEnc.toString().getBytes(csinfo.csName))) {
            log.println("      Error: String().getBytes() failed");
            rv = false;
        }
        return rv;
    }

    private boolean run() throws Exception {
        boolean rv = true;
        shiftHackDBCS = csinfo.type.equals("ebcdic");    

        
        rv &= testStringConv();

        
        clearTests();
        if (shiftHackDBCS) {
            testFor(2).put(new byte[] { 0x0e });
        }
        csinfo.mappings.forEach(e -> {
                if (e.cp2 != 0)
                    return;          
                byte[] bs = e.bs;
                char[] cc = Character.toChars(e.cp);
                testFor(bs.length).put(bs, cc);
            });
        if (shiftHackDBCS) {
            testFor(2).put(new byte[] { 0x0f });
        }
        for (int i = 0; i < maxBytesPerChar; i++) {
            rv &= tests[i].run(DECODE);
        }

        
        clearTests();
        if (shiftHackDBCS) {
            testFor(2).put(new byte[] { 0x0e });
        }
        csinfo.mappings.forEach(e -> {
                if (e.cp2 != 0)
                    return;          
                if (csinfo.nr != null && csinfo.nr.containsKey(e.bb))
                    return;          
                if (csinfo.c2b != null && csinfo.c2b.containsKey(e.cp))
                    return;          
                byte[] bs = e.bs;
                char[] cc = Character.toChars(e.cp);
                testFor(bs.length).put(bs, cc);
            });
        if (csinfo.c2b != null)
            csinfo.c2b.values().forEach(e -> {
                    byte[] bs = e.bs;
                    char[] cc = Character.toChars(e.cp);
                    testFor(bs.length).put(bs, cc);
                });
        if (shiftHackDBCS) {
            testFor(2).put(new byte[] { 0x0f });
        }
        for (int i = 0; i < maxBytesPerChar; i++) {
            rv &= tests[i].run(ENCODE);
        }
        return rv;
    }

    private static class Entry {
        byte[] bs;   
        int cp;      
        int cp2;     
        long bb;     
    }

    private final static int  UNMAPPABLE = 0xFFFD;
    private static final Pattern ptn = Pattern.compile("(?:0x)?(\\p{XDigit}++)\\s++(?:U\\+|0x)?(\\p{XDigit}++)(?:\\s++#.*)?");
    private static final int G_BS  = 1;
    private static final int G_CP  = 2;
    private static final int G_CP2 = 3;

    private static class CharsetInfo {
        Charset  cs;
        String   pkgName;
        String   clzName;
        String   csName;
        String   hisName;
        String   type;
        boolean  isInternal;
        Set<String> aliases = new HashSet<>();

        
        List<Entry> mappings;
        Map<Long, Entry> nr;       
        Map<Integer, Entry> c2b;   

        CharsetInfo(String csName, String clzName) {
            this.csName = csName;
            this.clzName = clzName;
        }

        private Entry parse(Matcher m) {
            Entry e = new Entry();
            e.bb = Long.parseLong(m.group(G_BS), 16);
            if (e.bb < 0x100)
                e.bs = new byte[] { (byte)e.bb };
            else
                e.bs = parseBytes(m.group(G_BS));
            e.cp = Integer.parseInt(m.group(G_CP), 16);
            if (G_CP2 <= m.groupCount() && m.group(G_CP2) != null)
               e.cp2 = Integer.parseInt(m.group(G_CP2), 16);
            else
               e.cp2 = 0;
            return e;
        }

        boolean loadMappings(Path dir) throws IOException {
            
            Path path = dir.resolve(clzName + ".map");
            if (!Files.exists(path)) {
                return false;
            }
            Matcher m = ptn.matcher("");
            mappings = Files.lines(path)
                .filter(ln -> !ln.startsWith("#") && m.reset(ln).lookingAt())
                .map(ln -> parse(m))
                .filter(e -> e.cp != UNMAPPABLE)  
                .collect(Collectors.toList());
            
            path = dir.resolve(clzName + ".nr");
            if (Files.exists(path)) {
                nr = Files.lines(path)
                    .filter(ln -> !ln.startsWith("#") && m.reset(ln).lookingAt())
                    .map(ln -> parse(m))
                    .collect(Collectors.toMap(e -> e.bb, Function.identity()));
            }
            
            path = dir.resolve(clzName + ".c2b");
            if (Files.exists(path)) {
                c2b = Files.lines(path)
                    .filter(ln -> !ln.startsWith("#") && m.reset(ln).lookingAt())
                    .map(ln -> parse(m))
                    .collect(Collectors.toMap(e -> e.cp, Function.identity()));
            }
            return true;
        }
    }

    private static Set<CharsetInfo> charsets(Path cslist) throws IOException {
        Set<CharsetInfo> charsets = new LinkedHashSet<>();
        Iterator<String> itr = Files.readAllLines(cslist).iterator();
        CharsetInfo cs = null;

        while (itr.hasNext()) {
            String line = itr.next();
            if (line.startsWith("#") || line.length() == 0) {
                continue;
            }
            String[] tokens = line.split("\\s+");
            if (tokens.length < 2) {
                continue;
            }
            if ("charset".equals(tokens[0])) {
                if (cs != null) {
                    charsets.add(cs);
                    cs = null;
                }
                if (tokens.length < 3) {
                    throw new RuntimeException("Error: incorrect charset line [" + line + "]");
                }
                cs = new CharsetInfo(tokens[1], tokens[2]);
            } else {
                String key = tokens[1];              
                switch (key) {
                    case "alias":
                        if (tokens.length < 3) {
                            throw new RuntimeException("Error: incorrect alias line [" + line + "]");
                        }
                        cs.aliases.add(tokens[2]);   
                        break;
                    case "package":
                        cs.pkgName = tokens[2];
                        break;
                    case "type":
                        cs.type = tokens[2];
                        break;
                    case "hisname":
                        cs.hisName = tokens[2];
                        break;
                    case "internal":
                        cs.isInternal = Boolean.parseBoolean(tokens[2]);
                        break;
                    default:  
                }
            }
        }
        if (cs != null) {
            charsets.add(cs);
        }
        return charsets;
    }

    public static void main(String args[]) throws Exception {
        Path dir = Paths.get(System.getProperty("test.src", ".") +
                             "/../../../../make/data/charsetmapping");
        if (!Files.exists(dir)) {
            
            log.println("Nothing done, not in a jdk repo: ");
            return;
        }
        if (args.length > 0 && "-v".equals(args[0])) {
            
            verbose = true;
        }

        int errors = 0;
        int tested = 0;
        int skipped = 0;
        int known = 0;

        for (CharsetInfo csinfo : charsets(dir.resolve("charsets"))) {
            String csname = csinfo.csName;

            if (csinfo.isInternal) {
                continue;
            }

            log.printf("%ntesting: %-16s", csname);

            if (!Charset.isSupported(csname)) {
                errors++;
                log.println("    [error: charset is not supported]");
                continue;
            }

            Charset cs = csinfo.cs = Charset.forName(csinfo.csName);
            
            if (!cs.name().equals(csinfo.csName)) {
                errors++;
                log.printf("    [error: wrong csname: " + csinfo.csName
                           + " vs " + cs.name() + "]");
            }
            
            if (!cs.aliases().equals(csinfo.aliases)) {
                errors++;
                log.printf("    [error wrong aliases]");
                if (verbose) {
                    log.println();
                    log.println("    expected: " + csinfo.aliases);
                    log.println("         got: " + cs.aliases());
                }
            }

            if (csinfo.type.equals("source")) {
                log.println("    [skipped: source based]");
                skipped++;
                continue;
            }

            if (!csinfo.loadMappings(dir)) {
                log.println("    [error loading mappings failed]");
                errors++;
                continue;
            }

            tested++;
            log.println();
            if (!new TestCharsetMapping(csinfo).run()) {

                
                if (csinfo.csName.equals("x-IBM948") ||
                    csinfo.csName.equals("x-IBM950") ||
                    csinfo.csName.equals("x-IBM937") ||
                    csinfo.csName.equals("x-IBM1383"))
                {
                    log.println("    [**** skipped, KNOWN nr/c2b mapping issue]");
                    known++;
                    continue;
                }

                errors++;
            }
        }

        log.println();
        log.println(tested + " charset" + plural(tested) + " tested, "
                    + skipped + " skipped, " + known + " known issue(s)");
        log.println();
        if (errors > 0)
            throw new Exception("Errors detected in "
                                + errors + " charset" + plural(errors));
    }
}
