



package vm.compiler.jbe.dead.dead16;




class doit {
    static final double pause1(double x) {
        for(int k = 1; k <= 10000; k++) {
            x = x + 1.0;
        }
        return x;
    }

    static final double pause2(double x) {
        for(int k = 1; k <= 10000; k++) {
            x = x - 1.0;
        }
        return x;
    }
}

public class dead16 {
    static double c = 1;
    static double z = 0;
    public static void main(String args[]) {

        System.out.println("un_optimized()="+un_optimized()+"; hand_optimized()="+hand_optimized());
        if (un_optimized() == hand_optimized()) {
            System.out.println("Test dead16 Passed.");
        } else {
            throw new Error("Test dead16 Failed: un_optimized()=" + un_optimized() + " != hand_optimized()=" + hand_optimized());
        }
    }

    static double un_optimized() {
        double x = 1;
        
        x = x + 0;
        x = x - 0;
        x = x * 1;
        x = x / 1;

        
        x = x + c;
        x = x - c;
        x = x * c;
        x = x / c;

        
        x = x + z;
        x = x - z;
        x = x * (z + c);

        
        x = doit.pause1(x);
        x = doit.pause2(x);
        x = doit.pause2(x);
        x = doit.pause1(x);
        x = doit.pause1(x);
        x = doit.pause1(x);
        x = doit.pause2(x);
        x = doit.pause1(x);
        x = doit.pause2(x);
        x = doit.pause2(x);
        x = doit.pause2(x);
        x = doit.pause1(x);

        return x;
    }

    
    static double hand_optimized() {
        int k;
        double x = 1;
        

        

        

        
        k = 10001;

        return x;
    }
}
