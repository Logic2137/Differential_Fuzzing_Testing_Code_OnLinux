import java.awt.datatransfer.DataFlavor;

public class NormalizeMimeTypeParameter {

    static class TestFlavor extends DataFlavor {

        public String normalizeMimeType(String mimeType) {
            return super.normalizeMimeType(mimeType);
        }

        public String normalizeMimeTypeParameter(String parameterName, String parameterValue) {
            return super.normalizeMimeTypeParameter(parameterName, parameterValue);
        }
    }

    static TestFlavor testFlavor;

    public static void main(String[] args) {
        testFlavor = new TestFlavor();
        String type = "TestType";
        String parameter = "TestParameter";
        String retValue = testFlavor.normalizeMimeTypeParameter(type, parameter);
        if (!retValue.equals(parameter)) {
            throw new RuntimeException("Test FAILED: " + retValue);
        }
    }
}
