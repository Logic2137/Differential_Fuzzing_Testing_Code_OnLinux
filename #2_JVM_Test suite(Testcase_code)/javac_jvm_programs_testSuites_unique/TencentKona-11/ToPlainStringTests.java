



import java.math.*;

public class ToPlainStringTests {
    public static void main(String argv[]) {
        String [][] testCases = {
            {"0",                       "0"},
            {"1",                       "1"},
            {"10",                      "10"},
            {"2e1",                     "20"},
            {"3e2",                     "300"},
            {"4e3",                     "4000"},
            {"5e4",                     "50000"},
            {"6e5",                     "600000"},
            {"7e6",                     "7000000"},
            {"8e7",                     "80000000"},
            {"9e8",                     "900000000"},
            {"1e9",                     "1000000000"},

            {".0",                      "0.0"},
            {".1",                      "0.1"},
            {".10",                     "0.10"},
            {"1e-1",                    "0.1"},
            {"1e-1",                    "0.1"},
            {"2e-2",                    "0.02"},
            {"3e-3",                    "0.003"},
            {"4e-4",                    "0.0004"},
            {"5e-5",                    "0.00005"},
            {"6e-6",                    "0.000006"},
            {"7e-7",                    "0.0000007"},
            {"8e-8",                    "0.00000008"},
            {"9e-9",                    "0.000000009"},
            {"9000e-12",                "0.000000009000"},

            {"9000e-22",                 "0.0000000000000000009000"},
            {"12345678901234567890",     "12345678901234567890"},
            {"12345678901234567890e22",  "123456789012345678900000000000000000000000"},
            {"12345678901234567890e-22", "0.0012345678901234567890"},
        };

        int errors = 0;
        for(String[] testCase: testCases) {
            BigDecimal bd = new BigDecimal(testCase[0]);
            String s;

            if (!(s=bd.toPlainString()).equals(testCase[1])) {
                errors++;
                System.err.println("Unexpected plain result ``" +
                                   s + "'' from BigDecimal " +
                                   bd);
            }
            bd = new BigDecimal("-"+testCase[0]);
            if (bd.signum()!=0 && !(s=(bd.toPlainString())).equals("-"+testCase[1])) {
                errors++;
                System.err.println("Unexpected plain result ``" +
                                   s + "'' from BigDecimal " +
                                   bd);
            }
        }

        if(errors > 0)
            throw new RuntimeException(errors + " errors during run.");
    }
}
