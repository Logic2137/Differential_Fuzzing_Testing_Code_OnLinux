import java.text.ParseException;

public class TestData {

    public static final char[][] unicodeIn = { { 0x0644, 0x064A, 0x0647, 0x0645, 0x0627, 0x0628, 0x062A, 0x0643, 0x0644, 0x0645, 0x0648, 0x0634, 0x0639, 0x0631, 0x0628, 0x064A, 0x061F }, { 0x4ED6, 0x4EEC, 0x4E3A, 0x4EC0, 0x4E48, 0x4E0D, 0x8BF4, 0x4E2D, 0x6587 }, { 0x0050, 0x0072, 0x006F, 0x010D, 0x0070, 0x0072, 0x006F, 0x0073, 0x0074, 0x011B, 0x006E, 0x0065, 0x006D, 0x006C, 0x0075, 0x0076, 0x00ED, 0x010D, 0x0065, 0x0073, 0x006B, 0x0079 }, { 0x05DC, 0x05DE, 0x05D4, 0x05D4, 0x05DD, 0x05E4, 0x05E9, 0x05D5, 0x05D8, 0x05DC, 0x05D0, 0x05DE, 0x05D3, 0x05D1, 0x05E8, 0x05D9, 0x05DD, 0x05E2, 0x05D1, 0x05E8, 0x05D9, 0x05EA }, { 0x092F, 0x0939, 0x0932, 0x094B, 0x0917, 0x0939, 0x093F, 0x0928, 0x094D, 0x0926, 0x0940, 0x0915, 0x094D, 0x092F, 0x094B, 0x0902, 0x0928, 0x0939, 0x0940, 0x0902, 0x092C, 0x094B, 0x0932, 0x0938, 0x0915, 0x0924, 0x0947, 0x0939, 0x0948, 0x0902 }, { 0x306A, 0x305C, 0x307F, 0x3093, 0x306A, 0x65E5, 0x672C, 0x8A9E, 0x3092, 0x8A71, 0x3057, 0x3066, 0x304F, 0x308C, 0x306A, 0x3044, 0x306E, 0x304B }, { 0x043F, 0x043E, 0x0447, 0x0435, 0x043C, 0x0443, 0x0436, 0x0435, 0x043E, 0x043D, 0x0438, 0x043D, 0x0435, 0x0433, 0x043E, 0x0432, 0x043E, 0x0440, 0x044F, 0x0442, 0x043F, 0x043E, 0x0440, 0x0443, 0x0441, 0x0441, 0x043A, 0x0438 }, { 0x0050, 0x006F, 0x0072, 0x0071, 0x0075, 0x00E9, 0x006E, 0x006F, 0x0070, 0x0075, 0x0065, 0x0064, 0x0065, 0x006E, 0x0073, 0x0069, 0x006D, 0x0070, 0x006C, 0x0065, 0x006D, 0x0065, 0x006E, 0x0074, 0x0065, 0x0068, 0x0061, 0x0062, 0x006C, 0x0061, 0x0072, 0x0065, 0x006E, 0x0045, 0x0073, 0x0070, 0x0061, 0x00F1, 0x006F, 0x006C }, { 0x4ED6, 0x5011, 0x7232, 0x4EC0, 0x9EBD, 0x4E0D, 0x8AAA, 0x4E2D, 0x6587 }, { 0x0054, 0x1EA1, 0x0069, 0x0073, 0x0061, 0x006F, 0x0068, 0x1ECD, 0x006B, 0x0068, 0x00F4, 0x006E, 0x0067, 0x0074, 0x0068, 0x1EC3, 0x0063, 0x0068, 0x1EC9, 0x006E, 0x00F3, 0x0069, 0x0074, 0x0069, 0x1EBF, 0x006E, 0x0067, 0x0056, 0x0069, 0x1EC7, 0x0074 }, { 0x0033, 0x5E74, 0x0042, 0x7D44, 0x91D1, 0x516B, 0x5148, 0x751F }, { 0x5B89, 0x5BA4, 0x5948, 0x7F8E, 0x6075, 0x002D, 0x0077, 0x0069, 0x0074, 0x0068, 0x002D, 0x0053, 0x0055, 0x0050, 0x0045, 0x0052, 0x002D, 0x004D, 0x004F, 0x004E, 0x004B, 0x0045, 0x0059, 0x0053 }, { 0x0048, 0x0065, 0x006C, 0x006C, 0x006F, 0x002D, 0x0041, 0x006E, 0x006F, 0x0074, 0x0068, 0x0065, 0x0072, 0x002D, 0x0057, 0x0061, 0x0079, 0x002D, 0x305D, 0x308C, 0x305E, 0x308C, 0x306E, 0x5834, 0x6240 }, { 0x3072, 0x3068, 0x3064, 0x5C4B, 0x6839, 0x306E, 0x4E0B, 0x0032 }, { 0x004D, 0x0061, 0x006A, 0x0069, 0x3067, 0x004B, 0x006F, 0x0069, 0x3059, 0x308B, 0x0035, 0x79D2, 0x524D }, { 0x30D1, 0x30D5, 0x30A3, 0x30FC, 0x0064, 0x0065, 0x30EB, 0x30F3, 0x30D0 }, { 0x305D, 0x306E, 0x30B9, 0x30D4, 0x30FC, 0x30C9, 0x3067 }, { 0xD800, 0xDF00, 0xD800, 0xDF01, 0xD800, 0xDF02, 0xD800, 0xDF03, 0xD800, 0xDF05, 0xD800, 0xDF06, 0xD800, 0xDF07, 0xD800, 0xDF09, 0xD800, 0xDF0A, 0xD800, 0xDF0B }, { 0xD800, 0xDF0D, 0xD800, 0xDF0C, 0xD800, 0xDF1E, 0xD800, 0xDF0F, 0xD800, 0xDF16, 0xD800, 0xDF15, 0xD800, 0xDF14, 0xD800, 0xDF12, 0xD800, 0xDF10, 0xD800, 0xDF20, 0xD800, 0xDF21 }, { 0x03b5, 0x03bb, 0x03bb, 0x03b7, 0x03bd, 0x03b9, 0x03ba, 0x03ac }, { 0x0062, 0x006f, 0x006e, 0x0121, 0x0075, 0x0073, 0x0061, 0x0127, 0x0127, 0x0061 }, { 0x043f, 0x043e, 0x0447, 0x0435, 0x043c, 0x0443, 0x0436, 0x0435, 0x043e, 0x043d, 0x0438, 0x043d, 0x0435, 0x0433, 0x043e, 0x0432, 0x043e, 0x0440, 0x044f, 0x0442, 0x043f, 0x043e, 0x0440, 0x0443, 0x0441, 0x0441, 0x043a, 0x0438 } };

    public static final String[] asciiIn = { "xn--egbpdaj6bu4bxfgehfvwxn", "xn--ihqwcrb4cv8a8dqg056pqjye", "xn--Proprostnemluvesky-uyb24dma41a", "xn--4dbcagdahymbxekheh6e0a7fei0b", "xn--i1baa7eci9glrd9b2ae1bj0hfcgg6iyaf8o0a1dig0cd", "xn--n8jok5ay5dzabd5bym9f0cm5685rrjetr6pdxa", "xn--b1abfaaepdrnnbgefbaDotcwatmq2g4l", "xn--PorqunopuedensimplementehablarenEspaol-fmd56a", "xn--ihqwctvzc91f659drss3x8bo0yb", "xn--TisaohkhngthchnitingVit-kjcr8268qyxafd2f1b9g", "xn--3B-ww4c5e180e575a65lsy2b", "xn---with-SUPER-MONKEYS-pc58ag80a8qai00g7n9n", "xn--Hello-Another-Way--fc4qua05auwb3674vfr0b", "xn--2-u9tlzr9756bt3uc0v", "xn--MajiKoi5-783gue6qz075azm5e", "xn--de-jg4avhby1noc0d", "xn--d9juau41awczczp", "XN--097CCDEKGHQJK", "XN--db8CBHEJLGH4E0AL", "xn--hxargifdar", "xn--bonusaa-5bb1da", "xn--b1abfaaepdrnnbgefbadotcwatmq2g4l" };

    public static final String[] domainNames = { "slip129-37-118-146.nc.us.ibm.net", "saratoga.pe.utexas.edu", "dial-120-45.ots.utexas.edu", "woo-085.dorms.waller.net", "hd30-049.hil.compuserve.com", "pem203-31.pe.ttu.edu", "56K-227.MaxTNT3.pdq.net", "dial-36-2.ots.utexas.edu", "slip129-37-23-152.ga.us.ibm.net", "ts45ip119.cadvision.com", "sdn-ts-004txaustP05.dialsprint.net", "bar-tnt1s66.erols.com", "101.st-louis-15.mo.dial-access.att.net", "h92-245.Arco.COM", "dial-13-2.ots.utexas.edu", "net-redynet29.datamarkets.com.ar", "ccs-shiva28.reacciun.net.ve", "7.houston-11.tx.dial-access.att.net", "ingw129-37-120-26.mo.us.ibm.net", "dialup6.austintx.com", "dns2.tpao.gov.tr", "slip129-37-119-194.nc.us.ibm.net", "cs7.dillons.co.uk.203.119.193.in-addr.arpa", "swprd1.innovplace.saskatoon.sk.ca", "bikini.bologna.maraut.it", "node91.subnet159-198-79.baxter.com", "cust19.max5.new-york.ny.ms.uu.net", "balexander.slip.andrew.cmu.edu", "pool029.max2.denver.co.dynip.alter.net", "cust49.max9.new-york.ny.ms.uu.net", "s61.abq-dialin2.hollyberry.com" };

    public static final String[] domainNames1Uni = { "\u0917\u0928\u0947\u0936.sanjose.ibm.com", "www.\u0121.com", "www.\u00C2\u00A4.com", "www.\u00C2\u00A3.com", "\u00C3\u00BC.com" };

    public static final String[] domainNamesToASCIIOut = { "xn--31b8a2bwd.sanjose.ibm.com", "www.xn--vea.com", "www.xn--bba7j.com", "www.xn--9a9j.com", "xn--14-ria7423a.com" };

    public static final String[] domainNamesToUnicodeOut = { "\u0917\u0928\u0947\u0936.sanjose.ibm.com", "www.\u0121.com", "www.\u00E2\u00A4.com", "www.\u00E2\u00A3.com", "\u00E3\u0031\u2044\u0034.com" };

    public static class ErrorCase {

        public char[] unicode;

        public String ascii;

        public Exception expected;

        public boolean useSTD3ASCIIRules;

        public boolean testToUnicode;

        public boolean testLabel;

        ErrorCase(char[] uniIn, String asciiIn, Exception ex, boolean std3, boolean testToUni, boolean testlabel) {
            unicode = uniIn;
            ascii = asciiIn;
            expected = ex;
            useSTD3ASCIIRules = std3;
            testToUnicode = testToUni;
            testLabel = testlabel;
        }
    }

    public static final ErrorCase[] errorCases = { new ErrorCase(new char[] { 0x0077, 0x0077, 0x0077, 0x002e, 0xC138, 0xACC4, 0xC758, 0xBAA8, 0xB4E0, 0xC0AC, 0xB78C, 0xB4E4, 0xC774, 0x070F, 0xD55C, 0xAD6D, 0xC5B4, 0xB97C, 0xC774, 0xD574, 0xD55C, 0xB2E4, 0xBA74, 0x002e, 0x0063, 0x006f, 0x006d }, "www.XN--8mb5595fsoa28orucya378bqre2tcwop06c5qbw82a1rffmae0361dea96b.com", new ParseException("StringPrep PROHIBITED_ERROR", -1), false, true, true), new ErrorCase(new char[] { 0x0077, 0x0077, 0x0077, 0x002e, 0xC138, 0xACC4, 0xC758, 0xBAA8, 0xB4E0, 0xC0AC, 0xB78C, 0xB4E4, 0xC774, 0x0221, 0x0234, 0x002e, 0x0063, 0x006f, 0x006d }, "www.XN--6lA2Bz548Fj1GuA391Bf1Gb1N59Ab29A7iA.com", new ParseException("StringPrep UNASSIGNED_ERROR", -1), false, true, true), new ErrorCase(new char[] { 0x0077, 0x0077, 0x0077, 0x002e, 0xC138, 0xACC4, 0xC758, 0xBAA8, 0xB4E0, 0xC0AC, 0xB78C, 0xB4E4, 0xC774, 0x0644, 0x064A, 0x0647, 0xD55C, 0xAD6D, 0xC5B4, 0xB97C, 0xC774, 0xD574, 0xD55C, 0xB2E4, 0xBA74, 0x002e, 0x0063, 0x006f, 0x006d }, "www.xn--ghBGI4851OiyA33VqrD6Az86C4qF83CtRv93D5xBk15AzfG0nAgA0578DeA71C.com", new ParseException("StringPrep CHECK_BIDI_ERROR", -1), false, true, true), new ErrorCase(new char[] { 0x0077, 0x0077, 0x0077, 0x002e, 0x002D, 0xACC4, 0xC758, 0xBAA8, 0xB4E0, 0xC0AC, 0xB78C, 0xB4E4, 0xC774, 0x002E, 0xD55C, 0xAD6D, 0xC5B4, 0xB97C, 0xC774, 0xD574, 0xD55C, 0xB2E4, 0xBA74, 0x002e, 0x0063, 0x006f, 0x006d }, "www.xn----b95Ew8SqA315Ao5FbuMlnNmhA.com", new ParseException("StringPrep STD3_ASCII_RULES_ERROR", -1), true, true, false), new ErrorCase(new char[] { 0x0077, 0x0077, 0x0077, 0x002e, 0x0078, 0x006e, 0x002d, 0x002d, 0x002D, 0xACC4, 0xC758, 0xBAA8, 0xB4E0, 0xC0AC, 0xB78C, 0xB4E4, 0xC774, 0x002D, 0xD55C, 0xAD6D, 0xC5B4, 0xB97C, 0xC774, 0xD574, 0xD55C, 0xB2E4, 0xBA74, 0x002e, 0x0063, 0x006f, 0x006d }, "www.XY-----b91I0V65S96C2A355Cw1E5yCeQr19CsnP1mFfmAE0361DeA96B.com", new ParseException("StringPrep ACE_PREFIX_ERROR", -1), false, false, false), new ErrorCase(new char[] { 0x0077, 0x0077, 0x0077, 0x002e, 0xC138, 0xACC4, 0xC758, 0xBAA8, 0xB4E0, 0xC0AC, 0xB78C, 0xB4E4, 0xC774, 0xD55C, 0xAD6D, 0xC5B4, 0xB97C, 0xC774, 0xD574, 0xD55C, 0xB2E4, 0xBA74, 0xC5BC, 0xB9C8, 0xB098, 0xC88B, 0xC744, 0xAE4C, 0x002e, 0x0063, 0x006f, 0x006d }, "www.xn--989AoMsVi5E83Db1D2A355Cv1E0vAk1DwRv93D5xBh15A0Dt30A5JpSD879Ccm6FeA98C.com", new ParseException("StringPrep LABEL_TOO_LONG_ERROR", -1), false, true, true), new ErrorCase(new char[] { 0x0077, 0x0077, 0x0077, 0x002e, 0x0030, 0x0644, 0x064A, 0x0647, 0x0031, 0x002e, 0x0063, 0x006f, 0x006d }, "www.xn--01-tvdmo.com", new ParseException("StringPrep CHECK_BIDI_ERROR", -1), false, true, true), new ErrorCase(new char[] { 0x0077, 0x0077, 
    0x0077, 0x002e, 0x206C, 0x0644, 0x064A, 
    0x0647, 0x206D, 0x002e, 0x0063, 0x006f, 0x006d }, "www.XN--ghbgi278xia.com", new ParseException("StringPrep PROHIBITED_ERROR", -1), false, true, true), new ErrorCase(new char[] { 0x0077, 0x0077, 
    0x0077, 0x002e, 0x002D, 0x0041, 0x0042, 0x0043, 
    0x0044, 0x0045, 0x002e, 0x0063, 0x006f, 0x006d }, "www.-abcde.com", new ParseException("StringPrep STD3_ASCII_RULES_ERROR", -1), true, true, false), new ErrorCase(new char[] { 0x0077, 0x0077, 
    0x0077, 0x002e, 0x0041, 0x0042, 0x0043, 0x0044, 
    0x0045, 0x002D, 0x002e, 0x0063, 0x006f, 0x006d }, "www.abcde-.com", new ParseException("StringPrep STD3_ASCII_RULES_ERROR", -1), true, true, false), new ErrorCase(new char[] { 0x0077, 0x0077, 
    0x0077, 0x002e, 0x0041, 0x0042, 0x0043, 0x0044, 
    0x0045, 0x0040, 0x002e, 0x0063, 0x006f, 0x006d }, "www.abcde@.com", new ParseException("StringPrep STD3_ASCII_RULES_ERROR", -1), true, true, false) };

    public static final class ConformanceTestCase {

        String comment;

        String input;

        String output;

        String profile;

        int flags;

        Exception expected;

        private static byte[] getBytes(String in) {
            if (in == null) {
                return null;
            }
            byte[] bytes = new byte[in.length()];
            for (int i = 0; i < in.length(); i++) {
                bytes[i] = (byte) in.charAt(i);
            }
            return bytes;
        }

        ConformanceTestCase(String comt, String in, String out, String prof, int flg, Exception ex) {
            try {
                comment = comt;
                byte[] bytes = getBytes(in);
                input = new String(bytes, "UTF-8");
                bytes = getBytes(out);
                output = (bytes == null) ? null : new String(bytes, "UTF-8");
                profile = prof;
                flags = flg;
                expected = ex;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
    }

    public static final ConformanceTestCase[] conformanceTestCases = { new ConformanceTestCase("Case folding ASCII U+0043 U+0041 U+0046 U+0045", "\u0043\u0041\u0046\u0045", "\u0063\u0061\u0066\u0065", "Nameprep", 0, null), new ConformanceTestCase("Case folding 8bit U+00DF (german sharp s)", "\u00C3\u009F", "\u0073\u0073", "Nameprep", 0, null), new ConformanceTestCase("Non-ASCII multibyte space character U+1680", "\u00E1\u009A\u0080", null, "Nameprep", 0, new ParseException("StringPrep PROHIBITED_ERROR", -1)), new ConformanceTestCase("Non-ASCII 8bit control character U+0085", "\u00C2\u0085", null, "Nameprep", 0, new ParseException("StringPrep PROHIBITED_ERROR", -1)), new ConformanceTestCase("Non-ASCII multibyte control character U+180E", "\u00E1\u00A0\u008E", null, "Nameprep", 0, new ParseException("StringPrep PROHIBITED_ERROR", -1)), new ConformanceTestCase("Non-ASCII control character U+1D175", "\u00F0\u009D\u0085\u00B5", null, "Nameprep", 0, new ParseException("StringPrep PROHIBITED_ERROR", -1)), new ConformanceTestCase("Plane 0 private use character U+F123", "\u00EF\u0084\u00A3", null, "Nameprep", 0, new ParseException("StringPrep PROHIBITED_ERROR", -1)), new ConformanceTestCase("Plane 15 private use character U+F1234", "\u00F3\u00B1\u0088\u00B4", null, "Nameprep", 0, new ParseException("StringPrep PROHIBITED_ERROR", -1)), new ConformanceTestCase("Plane 16 private use character U+10F234", "\u00F4\u008F\u0088\u00B4", null, "Nameprep", 0, new ParseException("StringPrep PROHIBITED_ERROR", -1)), new ConformanceTestCase("Non-character code point U+8FFFE", "\u00F2\u008F\u00BF\u00BE", null, "Nameprep", 0, new ParseException("StringPrep PROHIBITED_ERROR", -1)), new ConformanceTestCase("Non-character code point U+10FFFF", "\u00F4\u008F\u00BF\u00BF", null, "Nameprep", 0, new ParseException("StringPrep PROHIBITED_ERROR", -1)), new ConformanceTestCase("Non-plain text character U+FFFD", "\u00EF\u00BF\u00BD", null, "Nameprep", 0, new ParseException("StringPrep PROHIBITED_ERROR", -1)), new ConformanceTestCase("Ideographic description character U+2FF5", "\u00E2\u00BF\u00B5", null, "Nameprep", 0, new ParseException("StringPrep PROHIBITED_ERROR", -1)), new ConformanceTestCase("Display property character U+0341", "\u00CD\u0081", "\u00CC\u0081", "Nameprep", 0, null), new ConformanceTestCase("Left-to-right mark U+200E", "\u00E2\u0080\u008E", "\u00CC\u0081", "Nameprep", 0, new ParseException("StringPrep PROHIBITED_ERROR", -1)), new ConformanceTestCase("Deprecated U+202A", "\u00E2\u0080\u00AA", "\u00CC\u0081", "Nameprep", 0, new ParseException("StringPrep PROHIBITED_ERROR", -1)), new ConformanceTestCase("Language tagging character U+E0001", "\u00F3\u00A0\u0080\u0081", "\u00CC\u0081", "Nameprep", 0, new ParseException("StringPrep PROHIBITED_ERROR", -1)), new ConformanceTestCase("Language tagging character U+E0042", "\u00F3\u00A0\u0081\u0082", null, "Nameprep", 0, new ParseException("StringPrep PROHIBITED_ERROR", -1)), new ConformanceTestCase("Bidi: RandALCat character U+05BE and LCat characters", "\u0066\u006F\u006F\u00D6\u00BE\u0062\u0061\u0072", null, "Nameprep", 0, new ParseException("StringPrep CHECK_BIDI_ERROR", -1)), new ConformanceTestCase("Bidi: RandALCat character U+FD50 and LCat characters", "\u0066\u006F\u006F\u00EF\u00B5\u0090\u0062\u0061\u0072", null, "Nameprep", 0, new ParseException("StringPrep CHECK_BIDI_ERROR", -1)), new ConformanceTestCase("Bidi: RandALCat character U+FB38 and LCat characters", "\u0066\u006F\u006F\u00EF\u00B9\u00B6\u0062\u0061\u0072", "\u0066\u006F\u006F \u00d9\u008e\u0062\u0061\u0072", "Nameprep", 0, null), new ConformanceTestCase("Bidi: RandALCat without trailing RandALCat U+0627 U+0031", "\u00D8\u00A7\u0031", null, "Nameprep", 0, new ParseException("StringPrep CHECK_BIDI_ERROR", -1)), new ConformanceTestCase("Bidi: RandALCat character U+0627 U+0031 U+0628", "\u00D8\u00A7\u0031\u00D8\u00A8", "\u00D8\u00A7\u0031\u00D8\u00A8", "Nameprep", 0, null), new ConformanceTestCase("Unassigned code point U+E0002", "\u00F3\u00A0\u0080\u0082", null, "Nameprep", 0, new ParseException("StringPrep UNASSIGNED_ERROR", -1)) };
}
