

import java.math.*;
public class IntegralDivisionTests {

    static int dividetoIntegralValueTests() {
        int failures = 0;

        
        


        
        BigDecimal [][] moreTestCases = {
            {new BigDecimal("11003"),   new BigDecimal("10"),   new BigDecimal("1100")},
            {new BigDecimal("11003"),   new BigDecimal("1e1"),  new BigDecimal("1100.0")},
            {new BigDecimal("1e9"),     new BigDecimal("1"),    new BigDecimal("1e9")},
            {new BigDecimal("1e9"),     new BigDecimal("1.00"), new BigDecimal("1e9")},
            {new BigDecimal("1e9"),     new BigDecimal("0.1"),  new BigDecimal("1e10")},
            {new BigDecimal("10e8"),    new BigDecimal("0.1"),  new BigDecimal("10e9")},
            {new BigDecimal("400e1"),   new BigDecimal("5"),    new BigDecimal("80e1")},
            {new BigDecimal("400e1"),   new BigDecimal("4.999999999"),  new BigDecimal("8e2")},
            {new BigDecimal("40e2"),    new BigDecimal("5"),    new BigDecimal("8e2")},
            {BigDecimal.valueOf(1, Integer.MIN_VALUE),
                BigDecimal.valueOf(1, -(Integer.MAX_VALUE & 0x7fffff00)),
                BigDecimal.valueOf(1, -256)},
        };

        for(BigDecimal [] testCase: moreTestCases) {
            BigDecimal quotient;
            if (! (quotient=testCase[0].divideToIntegralValue(testCase[1])).equals(testCase[2]) ){
                failures++;
                
                System.err.println();
                System.err.println("dividend  = " + testCase[0] + " scale = " + testCase[0].scale());
                System.err.println("divisor   = " + testCase[1] + " scale = " + testCase[1].scale());
                System.err.println("quotient  = " + quotient    + " scale = " + quotient.scale());
                System.err.println("expected  = " + testCase[2] + " scale = " + testCase[2].scale());
                
            }
        }

        return failures;
    }

    static int dividetoIntegralValueRoundedTests() {
        int failures = 0;

        BigDecimal dividend = new BigDecimal("11003");
        BigDecimal divisor = new BigDecimal("10");
        BigDecimal [] quotients = {     
            new BigDecimal("1100"),     
            null,                       
            new BigDecimal("11e2"),     
            new BigDecimal("110e1"),    
            new BigDecimal("1100"),     
        };
        failures += divideContextTestPrecs(dividend, divisor, quotients);

        dividend = new BigDecimal("11003");
        divisor = new BigDecimal("1e1");
        BigDecimal [] quotients2 = {    
            new BigDecimal("1100.0"),   
            null,                       
            new BigDecimal("11e2"),     
            new BigDecimal("110e1"),    
            new BigDecimal("1100"),     
            new BigDecimal("1100.0"),   
        };
        failures += divideContextTestPrecs(dividend, divisor, quotients2);

        dividend = new BigDecimal("1230000");
        divisor = new BigDecimal("100");
        BigDecimal [] quotients3 = {    
            new BigDecimal("12300"),    
            null,                       
            null,                       
            new BigDecimal("123e2"),    
            new BigDecimal("1230e1"),   
            new BigDecimal("12300"),    
        };
        failures += divideContextTestPrecs(dividend, divisor, quotients3);

        dividend = new BigDecimal("33");
        divisor  = new BigDecimal("3");
        BigDecimal [] quotients4 = {    
            new BigDecimal("11"),       
            null,                       
            new BigDecimal("11"),       
            new BigDecimal("11"),       
        };
        failures += divideContextTestPrecs(dividend, divisor, quotients4);

        dividend = new BigDecimal("34");
        divisor  = new BigDecimal("3");
        BigDecimal [] quotients5 = {    
            new BigDecimal("11"),       
            null,                       
            new BigDecimal("11"),       
            new BigDecimal("11"),       
        };
        failures += divideContextTestPrecs(dividend, divisor, quotients5);

        return failures;
    }

    static int divideContextTestPrecs(BigDecimal dividend,
                                      BigDecimal divisor,
                                      BigDecimal[] quotients)
    {
        int failures = 0;
        for(int i = 0; i < quotients.length; i++) {
            BigDecimal result = null;
            BigDecimal quotient = quotients[i];

            try {
                result = dividend.divideToIntegralValue(divisor,
                                                        new MathContext(i, RoundingMode.DOWN));
            } catch (ArithmeticException e) {
                if (quotient != null) {
                    failures++;
                    System.err.println();
                    System.err.println("Unexpected exception:");
                    System.err.println("dividend  = " + dividend     + " scale = " + dividend.scale());
                    System.err.println("divisor   = " + divisor      + " scale = " + divisor.scale());
                    System.err.println("expected  = " + quotient     + " scale = " + quotient.scale());
                }
            }

            if (quotient != null) {
                if (! result.equals(quotient)) {
                    failures++;
                    System.err.println();
                    System.err.println("Unexpected result:");
                    System.err.println("dividend  = " + dividend     + " scale = " + dividend.scale());
                    System.err.println("divisor   = " + divisor      + " scale = " + divisor.scale());
                    System.err.println("quotient  = " + result       + " scale = " + result.scale());
                    System.err.println("expected  = " + quotient     + " scale = " + quotient.scale());
                    System.err.println("precision = " + i);
                }
            } else {
                if (result != null) {
                    failures++;
                    System.err.println();
                    System.err.println("Unexpected unexceptional result:");
                    System.err.println("dividend  = " + dividend     + " scale = " + dividend.scale());
                    System.err.println("divisor   = " + divisor      + " scale = " + divisor.scale());
                    System.err.println("quotient  = " + result       + " scale = " + result.scale());
                    System.err.println("precision = " + i);
                }
            }

        }
        return failures;
    }


    static int divideContextTests(BigDecimal dividend,
                                  BigDecimal divisor,
                                  BigDecimal expected,
                                  MathContext mc) {
        int failures = 0;

        failures += divideContextTest(dividend,              divisor,          expected,                mc);
        failures += divideContextTest(dividend.negate(),     divisor.negate(), expected,                mc);

        if (expected != null) {
            failures += divideContextTest(dividend.negate(), divisor,          expected.negate(),       mc);
            failures += divideContextTest(dividend,          divisor.negate(), expected.negate(),       mc);
        }

        return failures;
    }


    static int divideContextTest(BigDecimal dividend,
                                 BigDecimal divisor,
                                 BigDecimal expected,
                                 MathContext mc)
    {
        int failures = 0;

        BigDecimal result = null;

        try {
            result = dividend.divideToIntegralValue(divisor, mc);
        } catch (ArithmeticException e) {
            if (expected != null) {
                failures++;
                System.err.println();
                System.err.println("Unexpected exception:");
                System.err.println("dividend  = " + dividend     + " scale = " + dividend.scale());
                System.err.println("divisor   = " + divisor      + " scale = " + divisor.scale());
                System.err.println("expected  = " + expected     + " scale = " + expected.scale());
                System.err.println("MathContext  = " + mc);
            }
        }

        if (expected != null) {
            if (! result.equals(expected)) {
                failures++;
                System.err.println();
                System.err.println("Unexpected result:");
                System.err.println("dividend  = " + dividend     + " scale = " + dividend.scale());
                System.err.println("divisor   = " + divisor      + " scale = " + divisor.scale());
                System.err.println("expected  = " + expected     + " scale = " + expected.scale());
                System.err.println("result    = " + result       + " scale = " + result.scale());
                System.err.println("MathContext  = " + mc);
            }
        } else {
            if (result != null) {
                failures++;
                System.err.println();
                System.err.println("Unexpected unexceptional result:");
                System.err.println("dividend  = " + dividend     + " scale = " + dividend.scale());
                System.err.println("divisor   = " + divisor      + " scale = " + divisor.scale());
                System.err.println("quotient  = " + result       + " scale = " + result.scale());
                System.err.println("MathConext = " + mc);
                }
        }

        return failures;
    }

    static int dividetoIntegralValueScalingTests() {
        int failures = 0;

        BigDecimal dividend = new BigDecimal("123456789000");
        BigDecimal divisor = BigDecimal.ONE;
        BigDecimal expected = new BigDecimal("123456789e3");
        MathContext mc = new MathContext(9,RoundingMode.DOWN);
        failures += divideContextTests(dividend, divisor, expected, mc);


        
        int [] precisions = {0, 2, 3, 4};
        dividend = new BigDecimal(100);
        divisor  = new BigDecimal(3);
        expected = new BigDecimal(33);

        for(RoundingMode rm: RoundingMode.values())
            for(int precision: precisions) {
                failures += divideContextTests(dividend, divisor, expected,
                                               new MathContext(precision, rm));
            }

        
        dividend = new BigDecimal(123000);
        divisor  = new BigDecimal(10);
        int[] precisions1 = {0, 1, 2, 3, 4, 5};
        BigDecimal[] expected1 = {
            new BigDecimal("12300"),
            null,
            null,
            new BigDecimal("123e2"),
            new BigDecimal("1230e1"),
            new BigDecimal("12300"),
        };

        for(RoundingMode rm: RoundingMode.values())
            for(int i = 0; i < precisions1.length; i++) {
                failures += divideContextTests(dividend, divisor,
                                               expected1[i],
                                               new MathContext(precisions1[i], rm));
            }

        
        dividend = new BigDecimal("123e3");
        divisor  = new BigDecimal(10);
        int[] precisions2 = {0, 1, 2, 3, 4, 5};
        BigDecimal[] expected2 = {
            new BigDecimal("123e2"),
            null,
            null,
            new BigDecimal("123e2"),
            new BigDecimal("123e2"),
            new BigDecimal("123e2"),
        };

        for(RoundingMode rm: RoundingMode.values())
            for(int i = 0; i < precisions2.length; i++) {
                failures += divideContextTests(dividend, divisor,
                                               expected2[i],
                                               new MathContext(precisions2[i], rm));
            }


        
        dividend = new BigDecimal("123000");
        divisor  = new BigDecimal("1e1");
        int[] precisions3 = {0, 1, 2, 3, 4, 5, 6};
        BigDecimal[] expected3 = {
            new BigDecimal("12300.0"),
            null,
            null,
            new BigDecimal("123e2"),
            new BigDecimal("1230e1"),
            new BigDecimal("12300"),
            new BigDecimal("12300.0"),
        };

        for(RoundingMode rm: RoundingMode.values())
            for(int i = 0; i < precisions3.length; i++) {
                failures += divideContextTests(dividend, divisor,
                                               expected3[i],
                                               new MathContext(precisions3[i], rm));
            }



        return failures;
    }

    public static void main(String argv[]) {
        int failures = 0;

        failures += dividetoIntegralValueTests();
        failures += dividetoIntegralValueRoundedTests();
        failures += dividetoIntegralValueScalingTests();

        if (failures > 0) {
            System.err.println("Encountered " + failures +
                               " failures while testing integral division.");
            throw new RuntimeException();
        }
    }
}
