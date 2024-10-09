
package gc.g1.plab.lib;

import java.util.ArrayList;


public class AppPLABEvacuationFailure {

    public static final int CHUNK = 10000;
    public static ArrayList<Object> arr = new ArrayList<>();

    public static void main(String[] args) {
        System.gc();
        
        try {
            while (true) {
                arr.add(new byte[CHUNK]);
            }
        } catch (OutOfMemoryError oome) {
            arr.clear();
        }
        
        try {
            while (true) {
                arr.add(new byte[CHUNK]);
            }
        } catch (OutOfMemoryError oome) {
            arr.clear();
        }
    }
}
