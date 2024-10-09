



public class Constants {
    
    public static void main(String[] args) throws Exception {
        int i = 0;
        switch (i) {
        case (int)Double.NaN:                   
            System.out.println("Double.NaN is a constant!");
            break;
        case (int)Double.MIN_VALUE + 1:         
            System.out.println("Double.MIN_VALUE is a constant!");
            break;
        case (int)Double.MIN_NORMAL + 2:        
            System.out.println("Double.MIN_NORMAL is a constant!");
            break;
        case Double.MIN_EXPONENT:               
            System.out.println("Double.MIN_EXPONENT is a constant!");
            break;
        case Double.MAX_EXPONENT:               
            System.out.println("Double.MAX_EXPONENT is a constant!");
            break;
        case (int)Double.MAX_VALUE - 1:         
            System.out.println("Double.MAX_VALUE is a constant!");
            break;
        case (int)Double.POSITIVE_INFINITY:     
            System.out.println("Double.POSITIVE_INFINITY is a constant!");
            break;
        case (int)Double.NEGATIVE_INFINITY:     
            System.out.println("Double.NEGATIVE_INFINITY is a constant!");
            break;
        }
    }
}
