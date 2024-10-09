



package compiler.loopopts;

public class TestOverunrolling {

    public static Object test(int arg) {
        Object arr[] = new Object[3];
        int lim = (arg & 3);
        
        
        
        
        
        
        
        for (int i = 0; i < lim; ++i) {
            arr[i] = new Object();
        }
        
        return arr;
    }

    public static void main(String args[]) {
        for (int i = 0; i < 42; ++i) {
            test(i);
        }
    }
}

