



public class ToStringCache {

    
    
    

    public static void main(String[] args) throws Exception {
        String original = "The original String";

        StringBuffer sb = new StringBuffer(original);

        String a = sb.toString();
        checkEqual(a, original);

        String b = sb.toString();
        checkEqual(a, b);

        

        sb.setLength(12);
        b = sb.toString();
        checkUnequal(a, b);
        a = b;

        sb.setCharAt(0, 'X');
        b = sb.toString();
        checkUnequal(a, b);
        a = b;

        sb.append(new Character('X'));
        b = sb.toString();
        checkUnequal(a, b);
        a = b;

        sb.append("More text");
        b = sb.toString();
        checkUnequal(a, b);
        a = b;

        sb.append(sb);
        b = sb.toString();
        checkUnequal(a, b);
        a = b;

        sb.append(new StringBuilder("Build"));
        b = sb.toString();
        checkUnequal(a, b);
        a = b;

        sb.append(new StringBuilder("Build2"), 0, 1);
        b = sb.toString();
        checkUnequal(a, b);
        a = b;

        sb.append(new char[] { 'a', 'b' });
        b = sb.toString();
        checkUnequal(a, b);
        a = b;

        sb.append(true);
        b = sb.toString();
        checkUnequal(a, b);
        a = b;

        sb.append('c');
        b = sb.toString();
        checkUnequal(a, b);
        a = b;

        sb.append(23);
        b = sb.toString();
        checkUnequal(a, b);
        a = b;

        sb.appendCodePoint(Character.codePointAt(new char[] { 'X'}, 0));
        b = sb.toString();
        checkUnequal(a, b);
        a = b;

        sb.append(1L);
        b = sb.toString();
        checkUnequal(a, b);
        a = b;

        sb.append(1.0f);
        b = sb.toString();
        checkUnequal(a, b);
        a = b;

        sb.append(1.0d);
        b = sb.toString();
        checkUnequal(a, b);
        a = b;

        sb.delete(0, 5);
        b = sb.toString();
        checkUnequal(a, b);
        a = b;

        sb.deleteCharAt(0);
        b = sb.toString();
        checkUnequal(a, b);
        a = b;

        sb.replace(0,2, "123");
        b = sb.toString();
        checkUnequal(a, b);
        a = b;

        sb.insert(0, new char[] { 'a', 'b', 'c'}, 0, 3);
        b = sb.toString();
        checkUnequal(a, b);
        a = b;

        sb.insert(0, new Object());
        b = sb.toString();
        checkUnequal(a, b);
        a = b;

        sb.insert(0, "abc");
        b = sb.toString();
        checkUnequal(a, b);
        a = b;

        sb.insert(0, new char[] { 'a', 'b', 'c' });
        b = sb.toString();
        checkUnequal(a, b);
        a = b;

        sb.insert(0, new StringBuilder("Build"));
        b = sb.toString();
        checkUnequal(a, b);
        a = b;

        sb.insert(0, new StringBuilder("Build"), 0, 1);
        b = sb.toString();
        checkUnequal(a, b);
        a = b;

        sb.insert(0, false);
        b = sb.toString();
        checkUnequal(a, b);
        a = b;

        sb.insert(0, 'X');
        b = sb.toString();
        checkUnequal(a, b);
        a = b;

        sb.insert(0, 1);
        b = sb.toString();
        checkUnequal(a, b);
        a = b;

        sb.insert(0, 1L);
        b = sb.toString();
        checkUnequal(a, b);
        a = b;

        sb.insert(0, 1.0f);
        b = sb.toString();
        checkUnequal(a, b);
        a = b;

        sb.insert(0, 1.0d);
        b = sb.toString();
        checkUnequal(a, b);
        a = b;

        sb.reverse();
        b = sb.toString();
        checkUnequal(a, b);

        

        sb.append((String)null);
        b = sb.toString();
        checkUnequal(a, b);
        a = b;

        sb.append((StringBuffer)null);
        b = sb.toString();
        checkUnequal(a, b);
        a = b;

        sb.append((StringBuilder)null);
        b = sb.toString();
        checkUnequal(a, b);
        a = b;

        sb.append((CharSequence)null);
        b = sb.toString();
        checkUnequal(a, b);
        a = b;

        

        
        sb = new StringBuffer(original);
        a = sb.toString();
        b = sb.toString();
        checkEqual(a, b);

        int l = sb.length();
        b = sb.toString();
        checkEqual(a, b);

        int cap = sb.capacity();
        b = sb.toString();
        checkEqual(a, b);

        sb.ensureCapacity(100);
        b = sb.toString();
        checkEqual(a, b);

        sb.trimToSize();
        b = sb.toString();
        checkEqual(a, b);

        char c = sb.charAt(1);
        b = sb.toString();
        checkEqual(a, b);

        int cp = sb.codePointAt(1);
        b = sb.toString();
        checkEqual(a, b);

        cp = sb.codePointBefore(2);
        b = sb.toString();
        checkEqual(a, b);

        int count = sb.codePointCount(0,1);
        b = sb.toString();
        checkEqual(a, b);

        count = sb.offsetByCodePoints(0, 1);
        b = sb.toString();
        checkEqual(a, b);

        sb.getChars(0, 1, new char[2], 0);
        b = sb.toString();
        checkEqual(a, b);

        String sub = sb.substring(0);
        b = sb.toString();
        checkEqual(a, b);

        CharSequence cs = sb.subSequence(0,1);
        b = sb.toString();
        checkEqual(a, b);

        sub = sb.substring(0, 3);
        b = sb.toString();
        checkEqual(a, b);

        int index = sb.indexOf("rig");
        b = sb.toString();
        checkEqual(a, b);

        index = sb.indexOf("rig", 2);
        b = sb.toString();
        checkEqual(a, b);

        index = sb.lastIndexOf("rig");
        b = sb.toString();
        checkEqual(a, b);

        index = sb.lastIndexOf("rig", 3);
        b = sb.toString();
        checkEqual(a, b);

    }

    private static void checkEqual(String s1, String s2) {
        if (!s1.equals(s2))
            throw new RuntimeException("Unmatched strings: s1 = "
                                       + s1 + " s2 = " + s2);
    }
    private static void checkUnequal(String s1, String s2) {
        if (s1.equals(s2))
            throw new RuntimeException("Unexpected matched strings: " + s1);
    }
}
