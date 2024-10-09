

package nsk.jdi.ReferenceType.visibleMethods;



class visibmethod002aClassForCheck {

    
    public void ClassForCheck() {
    }

    
    static void s_void_method() {}
    static long    s_long_method() {return 100;}
    static String  s_string_method() {return "return";}
    static Object  s_object_method() {return new Object();}
    static long[]  s_prim_array_method() {return new long[100];}
    static Object[]  s_ref_array_method() {return new Object[100];}

    static void s_void_par_method(boolean z) {}
    static long    s_long_par_method(long l) {return 100;}
    static String  s_string_par_method(String s) {return "return";}
    static Object  s_object_par_method(Object obj) {return new Object();}
    static long[]  s_prim_array_par_method(long[] la) {return new long[100];}
    static Object[]  s_ref_array_par_method(Object[] obja) {return new Object[100];}


    
    void i_void_method() {}
    long    i_long_method() {return 100;}
    String  i_string_method() {return "return";}
    Object  i_object_method() {return new Object();}
    long[]  i_prim_array_method() {return new long[100];}
    Object[]  i_ref_array_method() {return new Object[100];}

    void i_void_par_method(boolean z) {}
    long    i_long_par_method(long l) {return 100;}
    String  i_string_par_method(String s) {return "return";}
    Object  i_object_par_method(Object obj) {return new Object();}
    long[]  i_prim_array_par_method(long[] la) {return new long[100];}
    Object[]  i_ref_array_par_method(Object[] obja) {return new Object[100];}

    
    static {}
}
