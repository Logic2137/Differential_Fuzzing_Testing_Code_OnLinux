
package compiler.codegen;

public class Test6431242 {

    int _len = 8;

    int[] _arr_i = new int[_len];

    long[] _arr_l = new long[_len];

    int[] _arr_i_cp = new int[_len];

    long[] _arr_l_cp = new long[_len];

    int _k = 0x12345678;

    int _j = 0;

    int _ir = 0x78563412;

    int _ir1 = 0x78563413;

    int _ir2 = 0x79563412;

    long _m = 0x123456789abcdef0L;

    long _l = 0L;

    long _lr = 0xf0debc9a78563412L;

    long _lr1 = 0xf0debc9a78563413L;

    long _lr2 = 0xf1debc9a78563412L;

    void init() {
        for (int i = 0; i < _arr_i.length; i++) {
            _arr_i[i] = _k;
            _arr_l[i] = _m;
        }
    }

    public int test_int_reversed(int i) {
        return Integer.reverseBytes(i);
    }

    public long test_long_reversed(long i) {
        return Long.reverseBytes(i);
    }

    public void test_copy_ints(int[] dst, int[] src) {
        for (int i = 0; i < src.length; i++) {
            dst[i] = Integer.reverseBytes(src[i]);
        }
    }

    public void test_copy_ints_reversed(int[] dst, int[] src) {
        for (int i = 0; i < src.length; i++) {
            dst[i] = 1 + Integer.reverseBytes(src[i]);
        }
    }

    public void test_copy_ints_store_reversed(int[] dst, int[] src) {
        for (int i = 0; i < src.length; i++) {
            dst[i] = Integer.reverseBytes(1 + src[i]);
        }
    }

    public void test_copy_longs(long[] dst, long[] src) {
        for (int i = 0; i < src.length; i++) {
            dst[i] = Long.reverseBytes(src[i]);
        }
    }

    public void test_copy_longs_reversed(long[] dst, long[] src) {
        for (int i = 0; i < src.length; i++) {
            dst[i] = 1 + Long.reverseBytes(src[i]);
        }
    }

    public void test_copy_longs_store_reversed(long[] dst, long[] src) {
        for (int i = 0; i < src.length; i++) {
            dst[i] = Long.reverseBytes(1 + src[i]);
        }
    }

    public void test() throws Exception {
        int up_limit = 90000;
        for (int loop = 0; loop < up_limit; loop++) {
            _j = test_int_reversed(_k);
            if (_j != _ir) {
                throw new Exception("Interger.reverseBytes failed " + _j + " iter " + loop);
            }
            _l = test_long_reversed(_m);
            if (_l != _lr) {
                throw new Exception("Long.reverseBytes failed " + _l + " iter " + loop);
            }
        }
        for (int loop = 0; loop < up_limit; loop++) {
            test_copy_ints(_arr_i_cp, _arr_i);
            for (int j = 0; j < _arr_i.length; j++) {
                if (_arr_i_cp[j] != _ir) {
                    throw new Exception("Interger.reverseBytes failed test_copy_ints iter " + loop);
                }
            }
            test_copy_ints_reversed(_arr_i_cp, _arr_i);
            for (int j = 0; j < _arr_i.length; j++) {
                if (_arr_i_cp[j] != _ir1) {
                    throw new Exception("Interger.reverseBytes failed test_copy_ints_reversed iter " + loop);
                }
            }
            test_copy_ints_store_reversed(_arr_i_cp, _arr_i);
            for (int j = 0; j < _arr_i.length; j++) {
                if (_arr_i_cp[j] != _ir2) {
                    throw new Exception("Interger.reverseBytes failed test_copy_ints_store_reversed iter " + loop);
                }
            }
            test_copy_longs(_arr_l_cp, _arr_l);
            for (int j = 0; j < _arr_i.length; j++) {
                if (_arr_l_cp[j] != _lr) {
                    throw new Exception("Long.reverseBytes failed test_copy_longs iter " + loop);
                }
            }
            test_copy_longs_reversed(_arr_l_cp, _arr_l);
            for (int j = 0; j < _arr_i.length; j++) {
                if (_arr_l_cp[j] != _lr1) {
                    throw new Exception("Long.reverseBytes failed test_copy_longs_reversed iter " + loop);
                }
            }
            test_copy_longs_store_reversed(_arr_l_cp, _arr_l);
            for (int j = 0; j < _arr_i.length; j++) {
                if (_arr_l_cp[j] != _lr2) {
                    throw new Exception("Long.reverseBytes failed test_copy_longs_store_reversed iter " + loop);
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            Test6431242 t = new Test6431242();
            t.init();
            t.test();
            System.out.println("Passed");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed");
        }
    }
}
