



package compiler.codegen;

public class Test6942326 {

    static String[] strings = new String[1024];
    private static final int ITERATIONS = 100000;

    public static void main(String[] args) {

        long start_total = System.currentTimeMillis();

        
        String a = " 1111111111111xx1111111111111xx11y"; 
        String b =  "1111111111111xx1111111111111xx11y";
        test_varsub_indexof(a, b);

        
        a = " 1111111111111xx1111111111111xx1y";
        b =  "1111111111111xx1111111111111xx1y";
        test_varsub_indexof(a, b);

        
        a = " 1111111111111xx1y";
        b =  "1111111111111xx1y";
        test_varsub_indexof(a, b);

        
        a = " 111111111111xx1y";
        b =  "111111111111xx1y";
        test_varsub_indexof(a, b);

        
        a = " 1111xx1y";
        b =  "1111xx1y";
        test_varsub_indexof(a, b);

        
        a = " 111xx1y";
        b =  "111xx1y";
        test_varsub_indexof(a, b);



        
        a =                 "1111111111111xx1x";
        b = " 1111111111111xx1111111111111xx1x"; 
        test_varstr_indexof(a, b);

        
        a =                  "111111111111xx1x";
        b = " 1111111111111xx1111111111111xx1x";
        test_varstr_indexof(a, b);

        
        a =                         "11111xx1x";
        b = " 1111111111111xx1111111111111xx1x";
        test_varstr_indexof(a, b);

        
        a =                          "1111xx1x";
        b = " 1111111111111xx1111111111111xx1x";
        test_varstr_indexof(a, b);

        
        a =                              "xx1x";
        b = " 1111111111111xx1111111111111xx1x";
        test_varstr_indexof(a, b);

        
        a =                               "x1x";
        b = " 1111111111111xx1111111111111xx1x";
        test_varstr_indexof(a, b);

        
        a =                                "1y";
        b = " 1111111111111xx1111111111111xx1y";
        test_varstr_indexof(a, b);



        
        a = " 1111111111111xx1111111111111xx11z"; 
        b =  "1111111111111xx1111111111111xx11y";
        test_missub_indexof(a, b);

        
        a = " 1111111111111xx1111111111111xx1z";
        b =  "1111111111111xx1111111111111xx1y";
        test_missub_indexof(a, b);

        
        a = " 1111111111111xx1z";
        b =  "1111111111111xx1y";
        test_missub_indexof(a, b);

        
        a = " 111111111111xx1z";
        b =  "111111111111xx1y";
        test_missub_indexof(a, b);

        
        a = " 1111xx1z";
        b =  "1111xx1y";
        test_missub_indexof(a, b);

        
        a = " 111xx1z";
        b =  "111xx1y";
        test_missub_indexof(a, b);



        

        
        b = " 1111111111111xx1111111111111xx1x"; 
        TestCon tc = new TestCon17();
        test_consub_indexof(tc, b);

        
        b = " 1111111111111xx1111111111111xx1x";
        tc = new TestCon16();
        test_consub_indexof(tc, b);

        
        b = " 1111111111111xx1111111111111xx1x";
        tc = new TestCon9();
        test_consub_indexof(tc, b);

        
        b = " 1111111111111xx1111111111111xx1x";
        tc = new TestCon8();
        test_consub_indexof(tc, b);

        
        b = " 1111111111111xx1111111111111xx1x";
        tc = new TestCon4();
        test_consub_indexof(tc, b);

        
        b = " 1111111111111xx1111111111111xx1x";
        tc = new TestCon3();
        test_consub_indexof(tc, b);

        
        b = " 1111111111111xx1111111111111xx1y";
        tc = new TestCon2();
        test_consub_indexof(tc, b);

        
        b = " 1111111111111xx1111111111111xx1y";
        tc = new TestCon1();
        test_consub_indexof(tc, b);


        
        b = " 1111111111111xx1111111111111xx1z"; 
        tc = new TestCon17();
        test_conmis_indexof(tc, b);

        
        b = " 1111111111111xx1111111111111xx1z";
        tc = new TestCon16();
        test_conmis_indexof(tc, b);

        
        b = " 1111111111111xx1111111111111xx1z";
        tc = new TestCon9();
        test_conmis_indexof(tc, b);

        
        b = " 1111111111111xx1111111111111xx1z";
        tc = new TestCon8();
        test_conmis_indexof(tc, b);

        
        b = " 1111111111111xx1111111111111xx1z";
        tc = new TestCon4();
        test_conmis_indexof(tc, b);

        
        b = " 1111111111111xx1111111111111xx1z";
        tc = new TestCon3();
        test_conmis_indexof(tc, b);

        
        b = " 1111111111111xx1111111111111xx1z";
        tc = new TestCon2();
        test_conmis_indexof(tc, b);

        
        b = " 1111111111111xx1111111111111xx1z";
        tc = new TestCon1();
        test_conmis_indexof(tc, b);

        long end_total = System.currentTimeMillis();
        System.out.println("End run time: " + (end_total - start_total));

    }

    public static long test_init(String a, String b) {
        for (int i = 0; i < 512; i++) {
            strings[i * 2] = new String(b.toCharArray());
            strings[i * 2 + 1] = new String(a.toCharArray());
        }
        System.out.print(a.length() + " " + b.length() + " ");
        return System.currentTimeMillis();
    }

    public static void test_end(String a, String b, int v, int expected, long start) {
        long end = System.currentTimeMillis();
        int res = (v/ITERATIONS);
        System.out.print(" " + res);
        System.out.println(" time:" + (end - start));
        if (res != expected) {
            System.out.println("wrong indexOf result: " + res + ", expected " + expected);
            System.out.println("\"" + b + "\".indexOf(\"" + a + "\")");
            System.exit(97);
        }
    }

    public static int test_subvar() {
        int s = 0;
        int v = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            v += strings[s].indexOf(strings[s + 1]);
            s += 2;
            if (s >= strings.length) s = 0;
        }
        return v;
    }

    public static void test_varsub_indexof(String a, String b) {
        System.out.println("Start search variable size substring in string (" + b.length() + " chars)");
        long start_it = System.currentTimeMillis();
        int limit = 1; 
        while (a.length() > limit) {
            a = a.substring(1);
            long start = test_init(a, b);
            int v = test_subvar();
            test_end(a, b, v, (b.length() - a.length()), start);
        }
        long end_it = System.currentTimeMillis();
        System.out.println("End search variable size substring in string (" + b.length() + " chars), time: " + (end_it - start_it));
    }

    public static void test_varstr_indexof(String a, String b) {
        System.out.println("Start search substring (" + a.length() + " chars) in variable size string");
        long start_it = System.currentTimeMillis();
        int limit = a.length();
        while (b.length() > limit) {
            b = b.substring(1);
            long start = test_init(a, b);
            int v = test_subvar();
            test_end(a, b, v, (b.length() - a.length()), start);
        }
        long end_it = System.currentTimeMillis();
        System.out.println("End search substring (" + a.length() + " chars) in variable size string, time: " + (end_it - start_it));
    }

    public static void test_missub_indexof(String a, String b) {
        System.out.println("Start search non matching variable size substring in string (" + b.length() + " chars)");
        long start_it = System.currentTimeMillis();
        int limit = 1; 
        while (a.length() > limit) {
            a = a.substring(1);
            long start = test_init(a, b);
            int v = test_subvar();
            test_end(a, b, v, (-1), start);
        }
        long end_it = System.currentTimeMillis();
        System.out.println("End search non matching variable size substring in string (" + b.length() + " chars), time: " + (end_it - start_it));
    }



    public static void test_consub_indexof(TestCon tc, String b) {
        System.out.println("Start search constant substring (" + tc.constr().length() + " chars)");
        long start_it = System.currentTimeMillis();
        int limit = tc.constr().length();
        while (b.length() > limit) {
            b = b.substring(1);
            long start = test_init(tc.constr(), b);
            int v = test_subcon(tc);
            test_end(tc.constr(), b, v, (b.length() - tc.constr().length()), start);
        }
        long end_it = System.currentTimeMillis();
        System.out.println("End search constant substring (" + tc.constr().length() + " chars), time: " + (end_it - start_it));
    }

    public static void test_conmis_indexof(TestCon tc, String b) {
        System.out.println("Start search non matching constant substring (" + tc.constr().length() + " chars)");
        long start_it = System.currentTimeMillis();
        int limit = tc.constr().length();
        while (b.length() > limit) {
            b = b.substring(1);
            long start = test_init(tc.constr(), b);
            int v = test_subcon(tc);
            test_end(tc.constr(), b, v, (-1), start);
        }
        long end_it = System.currentTimeMillis();
        System.out.println("End search non matching constant substring (" + tc.constr().length() + " chars), time: " + (end_it - start_it));
    }

    public static int test_subcon(TestCon tc) {
        int s = 0;
        int v = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            v += tc.indexOf(strings[s]);
            s += 2;
            if (s >= strings.length) s = 0;
        }
        return v;
    }

    private interface TestCon {
        public String constr();
        public int indexOf(String str);
    }

    
    private final static class TestCon17 implements TestCon {
        private static final String constr = "1111111111111xx1x";
        public String constr() { return constr; }
        public int indexOf(String str) { return str.indexOf(constr); }
    }

    
    private final static class TestCon16 implements TestCon {
        private static final String constr = "111111111111xx1x";
        public String constr() { return constr; }
        public int indexOf(String str) { return str.indexOf(constr); }
    }

    
    private final static class TestCon9 implements TestCon {
        private static final String constr = "11111xx1x";
        public String constr() { return constr; }
        public int indexOf(String str) { return str.indexOf(constr); }
    }

    
    private final static class TestCon8 implements TestCon {
        private static final String constr = "1111xx1x";
        public String constr() { return constr; }
        public int indexOf(String str) { return str.indexOf(constr); }
    }

    
    private final static class TestCon4 implements TestCon {
        private static final String constr = "xx1x";
        public String constr() { return constr; }
        public int indexOf(String str) { return str.indexOf(constr); }
    }

    
    private final static class TestCon3 implements TestCon {
        private static final String constr = "x1x";
        public String constr() { return constr; }
        public int indexOf(String str) { return str.indexOf(constr); }
    }

    
    private final static class TestCon2 implements TestCon {
        private static final String constr = "1y";
        public String constr() { return constr; }
        public int indexOf(String str) { return str.indexOf(constr); }
    }


    
    private final static class TestCon1 implements TestCon {
        private static final String constr = "y";
        public String constr() { return constr; }
        public int indexOf(String str) { return str.indexOf(constr); }
    }
}

