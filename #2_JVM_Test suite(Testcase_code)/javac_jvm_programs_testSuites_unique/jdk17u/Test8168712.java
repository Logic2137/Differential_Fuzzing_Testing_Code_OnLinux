
package compiler.runtime;

import java.util.*;

public class Test8168712 {

    static HashSet<Test8168712> m = new HashSet<>();

    public static void main(String[] args) {
        int i = 0;
        while (i++ < 15000) {
            test();
        }
    }

    static Test8168712 test() {
        return new Test8168712();
    }

    protected void finalize() {
        m.add(this);
    }
}
