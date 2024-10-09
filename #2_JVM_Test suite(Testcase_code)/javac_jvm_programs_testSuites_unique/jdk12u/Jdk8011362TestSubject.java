

package jdk.nashorn.test.models;


@SuppressWarnings("javadoc")
public class Jdk8011362TestSubject {
    
    public String overloaded(final String a, final String b) {
        return "overloaded(String, String)";
    }

    
    public String overloaded(final Double a, final Double b) {
        return "overloaded(Double, Double)";
    }

    
    
    public String overloaded(final Double a, final double b) {
        return "overloaded(Double, double)";
    }
}
