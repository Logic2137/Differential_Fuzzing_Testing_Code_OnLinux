



public class EmptyDescriptionTest {
    

    
    public int f1;

    
    
    public int f2;

    
    
    public int f3;

    
    
    public int f4;

    
    public int m1() { return 0; }

    
    
    public int m2() { return 0; }

    
    
    public int m3() { return 0; }

    
    
    public int m4() { return 0; };

    
    public static class Nested extends EmptyDescriptionTest {
         Nested() { }

        @Override
        public int m1() { return 1; }

        
        
        @Override
        public int m2() { return 1; }

        
        
        @Override
        public int m3() { return 1; }

    }
}
