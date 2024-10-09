



package compiler.escapeAnalysis;

public class Test6775880 {

    int cnt;
    int b[];
    String s;

    String test() {
        String res = "";
        for (int i = 0; i < cnt; i++) {
            if (i != 0) {
                res = res + ".";
            }
            res = res + b[i];
        }
        return res;
    }

    public static void main(String[] args) {
        Test6775880 t = new Test6775880();
        t.cnt = 3;
        t.b = new int[3];
        t.b[0] = 0;
        t.b[1] = 1;
        t.b[2] = 2;
        int j = 0;
        t.s = "";
        for (int i = 0; i < 10001; i++) {
            t.s = "c";
            t.s = t.test();
        }
        System.out.println("After s=" + t.s);
    }
}


