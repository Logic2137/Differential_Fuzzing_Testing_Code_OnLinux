

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.Class;
import java.lang.String;
import java.lang.System;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import sun.misc.Unsafe;
import sun.misc.Contended;


public class HasNonStatic {

    public static void main(String[] args) throws Exception {
        R1 r1 = new R1();
        R2 r2 = new R2();
        R3 r3 = new R3();
        R4 r4 = new R4();
    }

    public static class R1 {
        @Contended
        Object o;
    }

    @Contended
    public static class R2 {
        Object o;
    }

    @Contended
    public static class R3 {
    }

    public static class R4 extends R3 {
    }

}

