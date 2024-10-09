



public class SealedUnnamedModuleTest {

    public static void main(String args[]) throws Throwable {

        
        

        
        
        Class neptune = Class.forName("planets.Neptune");

        
        
        try {
            Class mars = Class.forName("planets.Mars");
            throw new RuntimeException("Expected IncompatibleClassChangeError exception not thrown");
        } catch (IncompatibleClassChangeError e) {
            if (!e.getMessage().contains("cannot inherit from sealed class")) {
                throw new RuntimeException("Wrong IncompatibleClassChangeError exception thrown: " + e.getMessage());
            }
        }

        
        
        try {
            Class pluto = Class.forName("asteroids.Pluto");
            throw new RuntimeException("Expected IncompatibleClassChangeError exception not thrown");
        } catch (IncompatibleClassChangeError e) {
            if (!e.getMessage().contains("cannot inherit from sealed class")) {
                throw new RuntimeException("Wrong IncompatibleClassChangeError exception thrown: " + e.getMessage());
            }
        }

        
        
        Class charon = Class.forName("asteroids.Charon");
    }
}
