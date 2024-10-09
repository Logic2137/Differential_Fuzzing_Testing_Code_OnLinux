



import java.util.Arrays;

public class TestLoopUnswitchingLostCastDependency {
    private static Object objectField;

    public static void main(String[] args) {
        Object[] array = new Object[100];
        Arrays.fill(array, new Object());
        for (int i = 0; i < 20_000; i++) {
            array[1] = null;
            test(array);
            array[1] = new Object();
            objectField = null;
            test(array);
            array[1] = new Object();
            objectField = new Object();
            test(array);
        }
    }

    private static void test(Object[] array) {
        Object o = objectField;
        Object o3 = array[1];
        int j = 0;
        for (int i = 1; i < 100; i *= 2) {
            Object o2 = array[i];
            
            if (o3 != null) {
                if (o2 == null) {
                }
                
                if (o != null) {
                    
                    
                    if (o.getClass() == Object.class) {
                    }
                    
                    
                    
                    
                    
                    
                    
                    if (j > 7) {

                    }
                    j++;
                }
            }
        }
    }
}
