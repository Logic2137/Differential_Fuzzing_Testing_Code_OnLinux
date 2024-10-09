



import java.lang.management.LockInfo;
public class GetModule {

    static {
        System.loadLibrary("GetModule");
    }

    static native Object callGetModule(java.lang.Class clazz);

    public static void main(String[] args) {
        Module module;
        Module javaBaseModule;

        
        java.lang.Integer primitive_int = 1;
        try {
            javaBaseModule = (Module)callGetModule(primitive_int.getClass());
            if (!javaBaseModule.getName().equals("java.base")) {
                throw new RuntimeException("Unexpected module name for primitive type: " +
                                           javaBaseModule.getName());
            }
        } catch(Throwable e) {
            throw new RuntimeException("Unexpected exception for Integer: " + e.toString());
        }

        
        int[] int_array = {1, 2, 3};
        try {
            javaBaseModule = (Module)callGetModule(int_array.getClass());
            if (!javaBaseModule.getName().equals("java.base")) {
                throw new RuntimeException("Unexpected module name for array of primitives: " +
                                           javaBaseModule.getName());
            }
        } catch(Throwable e) {
            throw new RuntimeException("Unexpected exception for [I: " + e.toString());
        }

        
        int[][] multi_int_array = { {1, 2, 3}, {4, 5, 6} };
        try {
            javaBaseModule = (Module)callGetModule(multi_int_array.getClass());
            if (!javaBaseModule.getName().equals("java.base")) {
                throw new RuntimeException("Unexpected module name for multi-dimensional array of primitives: " +
                                           javaBaseModule.getName());
            }
        } catch(Throwable e) {
            throw new RuntimeException("Unexpected exception for multi-dimensional Integer array: " + e.toString());
        }

        
        java.lang.String str = "abc";
        try {
            module = (Module)callGetModule(str.getClass());
            if (!module.getName().equals("java.base")) {
                throw new RuntimeException("Unexpected module name for class String: " +
                                           module.getName());
            }
        } catch(Throwable e) {
            throw new RuntimeException("Unexpected exception for String: " + e.toString());
        }

        
        java.lang.String[] str_array = {"a", "b", "c"};
        try {
            javaBaseModule = (Module)callGetModule(str_array.getClass());
            if (!javaBaseModule.getName().equals("java.base")) {
                throw new RuntimeException("Unexpected module name for array of Strings: " +
                                           javaBaseModule.getName());
            }
        } catch(Throwable e) {
            throw new RuntimeException("Unexpected exception for String array: " + e.toString());
        }

        
        java.lang.String[][] multi_str_array = { {"a", "b", "c"}, {"d", "e", "f"} };
        try {
            javaBaseModule = (Module)callGetModule(multi_str_array.getClass());
            if (!javaBaseModule.getName().equals("java.base")) {
                throw new RuntimeException("Unexpected module name for multi-dimensional array of Strings: " +
                                           javaBaseModule.getName());
            }
        } catch(Throwable e) {
            throw new RuntimeException("Unexpected exception for multidimensional String array: " + e.toString());
        }

        
        try {
            LockInfo li = new LockInfo("java.lang.Class", 57);
            module = (Module)callGetModule(li.getClass());
            if (!module.getName().equals("java.management")) {
                throw new RuntimeException("Unexpected module name for class LockInfo: " +
                                           module.getName());
            }
        } catch(Throwable e) {
            throw new RuntimeException("Unexpected exception for LockInfo: " + e.toString());
        }

        
        try {
            module = (Module)callGetModule(MyClassLoader.class);
            if (module == null || module.getName() != null) {
                throw new RuntimeException("Bad module for unnamed module");
            }
        } catch(Throwable e) {
            throw new RuntimeException("Unexpected exception for unnamed module: " + e.toString());
        }

        try {
            module = (Module)callGetModule(null);
            throw new RuntimeException("Failed to get expected NullPointerException");
        } catch(NullPointerException e) {
            
        }
    }

    static class MyClassLoader extends ClassLoader { }
}
