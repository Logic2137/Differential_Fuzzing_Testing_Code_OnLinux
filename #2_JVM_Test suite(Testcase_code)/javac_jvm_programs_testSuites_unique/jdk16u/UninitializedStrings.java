



public class UninitializedStrings {

    static {
        System.loadLibrary("UninitializedStrings");
    }

    native static void lengthTest();

    native static void charsTest();

    native static void utfLengthTest();

    native static void utfCharsTest();

    
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            throw new RuntimeException("invalid number of input arguments");
        }

        switch (args[0]) {
            case "length":
                lengthTest();
                break;
            case "chars":
                charsTest();
                break;
            case "utf_length":
                utfLengthTest();
                break;
            case "utf_chars":
                utfCharsTest();
                break;
            default:
                lengthTest();
                charsTest();
                utfLengthTest();
                utfCharsTest();
                break;
        }
    }
}
