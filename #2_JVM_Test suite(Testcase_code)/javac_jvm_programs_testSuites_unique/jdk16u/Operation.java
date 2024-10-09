

package pkg1;




public enum Operation {
    
    plus {
        
        double eval(double x, double y) { return x + y; }
    },
    minus {
        double eval(double x, double y) { return x - y; }
    },
    times {
        double eval(double x, double y) { return x * y; }
    },
    divided_by {
        double eval(double x, double y) { return x / y; }
    };

    
    abstract double eval(double x, double y);
}
