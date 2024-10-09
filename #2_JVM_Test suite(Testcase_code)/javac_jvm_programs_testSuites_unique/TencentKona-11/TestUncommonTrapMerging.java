



package compiler.rangechecks;

public class TestUncommonTrapMerging {

    public static void main(String[] args) throws Throwable {
        if (args.length < 1) {
            throw new RuntimeException("Not enough arguments!");
        }
        TestUncommonTrapMerging mytest = new TestUncommonTrapMerging();
        String testcase = args[0];
        if (testcase.equals("Test1")) {
            try {
                
                mytest.test(42);

            } catch (OutOfMemoryError e) {
                
            }
        } else if (testcase.equals("Test2")) {
            
            for (int i = 0; i < 100_000; i++) {
                mytest.test2(-1, 0);
            }

            
            
            
            
            for (int i = 0; i < 100_000; i++) {
                mytest.test3(0);
            }

            
            if (!mytest.test3(42)) {
                throw new RuntimeException("test2 returned through wrong path!");
            }
        }
    }

    public void test(int arg) throws Throwable {
        
        
        
        if (arg < 0) {
            throw new RuntimeException("Should not reach here");
        } else if (arg > 0) {
            throw new OutOfMemoryError();
        }
        throw new RuntimeException("Should not reach here");
    }

    public boolean test2(int arg, int value) {
        if (arg < 0) {
            if (value > 0) {
                
                return false;
            }
        } else if (arg > 0) {
            
            return true;
        }
        
        return false;
    }

    public boolean test3(int arg) {
        int i;
        for (i = 0; i < 1; ++i) { }
        
        return test2(arg, i);
    }
}
