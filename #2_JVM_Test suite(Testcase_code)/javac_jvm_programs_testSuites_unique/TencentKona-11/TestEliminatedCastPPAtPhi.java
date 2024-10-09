



package compiler.controldependency;

public class TestEliminatedCastPPAtPhi {

    static TestEliminatedCastPPAtPhi saved;
    static TestEliminatedCastPPAtPhi saved_not_null;

    int f;

    static int test(TestEliminatedCastPPAtPhi obj, int[] array, boolean flag) {
        int ret = array[0] + array[20];
        saved = obj;
        if (obj == null) {
            return ret;
        }
        saved_not_null = obj;

        
        
        
        int i = 0;
        for (; i < 10; i++);

        ret += array[i];

        TestEliminatedCastPPAtPhi res;
        if (flag) {
            
            res = saved;
        } else {
            
            res = saved_not_null;
        }
        

        
        
        
        

        
        
        

        
        
        

        
        
        
        

        
        
        
        
        
        
        

        return ret + res.f;
    }

    static public void main(String[] args) {
        int[] array = new int[100];
        TestEliminatedCastPPAtPhi obj = new TestEliminatedCastPPAtPhi();
        for (int i = 0; i < 20000; i++) {
            test(obj, array, (i%2) == 0);
        }
        test(null, array, true);
    }

}
