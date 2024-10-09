

import java.util.Arrays;

public class Checker {

    static void checkEq(String what, Object v, Object ref) throws Exception {
        if ((v != null) && v.equals(ref)) {
            System.out.println(what + ": ok (" + ref.toString() + ")");
        } else { throw new Exception(
                "invalid " + what + ", expected: \"" + ref + "\", got: \"" + v + "\"");
        }
    }

    static void checkEnumEq(String what, Object v, Object ref[]) throws Exception {
        if (v == null) { throw new Exception("null " + what); }
        if (!(v instanceof Object[])) { throw new Exception("invalid " + what); }
        if (Arrays.equals((Object []) v, ref)) { System.out.println(what + ": ok"); }
        else { throw new Exception("invalid " + what); }
    }
}
